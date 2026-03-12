package esg.esgdocbuilder.service;

import esg.esgdocbuilder.model.dto.response.UserProfileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserProfileService {
    UserProfileResponse getUserProfile();
    UserProfileResponse uploadUserPhoto(MultipartFile file);
}
