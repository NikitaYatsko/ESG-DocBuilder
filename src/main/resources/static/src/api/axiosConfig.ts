import axios, {
  AxiosError,
  AxiosInstance,
  AxiosRequestConfig,
  InternalAxiosRequestConfig,
} from 'axios';

// ─── Service URLs ────────────────────────────────────────────────────────────

const PRIMARY_URL = 'https://docbuilder-application.up.railway.app';
const FALLBACK_URL = 'https://docbuilder-copy-production.up.railway.app';
const DEV_URL = '/api';

// ─── localStorage key ────────────────────────────────────────────────────────

const ACTIVE_SERVICE_KEY = 'docbuilder_active_service';

type ServiceUrl = typeof PRIMARY_URL | typeof FALLBACK_URL;

// ─── Helpers ─────────────────────────────────────────────────────────────────

function isProd(): boolean {
  return import.meta.env.PROD;
}

/**
 * Returns the URL that was last successfully used (persisted across page loads).
 * Falls back to PRIMARY_URL if nothing is stored.
 */
function getStoredServiceUrl(): ServiceUrl {
  if (!isProd()) return PRIMARY_URL;
  const stored = localStorage.getItem(ACTIVE_SERVICE_KEY);
  if (stored === PRIMARY_URL || stored === FALLBACK_URL) return stored;
  return PRIMARY_URL;
}

function setStoredServiceUrl(url: ServiceUrl): void {
  localStorage.setItem(ACTIVE_SERVICE_KEY, url);
}

function getBaseUrl(): string {
  if (!isProd()) return DEV_URL;
  return getStoredServiceUrl();
}

function getFallbackUrl(currentBase: string): ServiceUrl | null {
  if (currentBase === PRIMARY_URL) return FALLBACK_URL;
  if (currentBase === FALLBACK_URL) return PRIMARY_URL;
  return null;
}

/**
 * Returns true when the error warrants a failover attempt:
 *   - Network-level failure (no response received)
 *   - 5xx server errors
 *   - Request timeout
 */
function isFailoverError(error: AxiosError): boolean {
  if (!error.response) {
    // Network error or timeout — no HTTP response received
    return true;
  }
  const status = error.response.status;
  return status >= 500 && status <= 599;
}

// ─── Axios instance ───────────────────────────────────────────────────────────

const api: AxiosInstance = axios.create({
  baseURL: getBaseUrl(),
  timeout: 15_000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

// ─── Request interceptor ──────────────────────────────────────────────────────
// Keep baseURL in sync with localStorage on every request so that if the
// stored service changes (e.g. after a failover) subsequent requests use it.

api.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  if (isProd()) {
    config.baseURL = getStoredServiceUrl();
  }
  return config;
});

// ─── Response interceptor — failover logic ────────────────────────────────────

api.interceptors.response.use(
  // Success path — persist the currently active service URL
  (response) => {
    if (isProd()) {
      const activeBase = response.config.baseURL as ServiceUrl | undefined;
      if (activeBase === PRIMARY_URL || activeBase === FALLBACK_URL) {
        setStoredServiceUrl(activeBase);
      }
    }
    return response;
  },

  // Error path — attempt failover when appropriate
  async (error: AxiosError) => {
    const originalConfig = error.config as (AxiosRequestConfig & { _retried?: boolean }) | undefined;

    // Guard: only attempt failover once per request, and only in production
    if (!isProd() || !originalConfig || originalConfig._retried) {
      return Promise.reject(error);
    }

    if (!isFailoverError(error)) {
      return Promise.reject(error);
    }

    const currentBase = (originalConfig.baseURL as string | undefined) ?? getStoredServiceUrl();
    const fallbackBase = getFallbackUrl(currentBase);

    if (!fallbackBase) {
      console.error('[API] No fallback URL available. Giving up.');
      return Promise.reject(error);
    }

    console.warn(
      `[API] Primary service unreachable (${currentBase}). ` +
        `Switching to fallback: ${fallbackBase}`
    );

    // Mark as retried to prevent infinite loops
    originalConfig._retried = true;
    originalConfig.baseURL = fallbackBase;

    try {
      const retryResponse = await api(originalConfig);

      // Failover succeeded — persist the fallback as the new active service
      setStoredServiceUrl(fallbackBase);
      console.info(`[API] Failover successful. Now using: ${fallbackBase}`);

      return retryResponse;
    } catch (fallbackError) {
      console.error(
        `[API] Fallback service also failed (${fallbackBase}). ` +
          'Both services are unavailable.'
      );
      // Restore primary so the next page load tries primary again
      setStoredServiceUrl(PRIMARY_URL);
      return Promise.reject(fallbackError);
    }
  }
);

export default api;
