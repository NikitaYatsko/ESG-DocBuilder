package esg.esgdocbuilder.service.impl;

import com.cloudinary.Api;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import esg.esgdocbuilder.constants.ApiErrorMessage;
import esg.esgdocbuilder.exception.exceptions.FileIsEmptyException;
import esg.esgdocbuilder.exception.exceptions.UserDoesNotExistsException;
import esg.esgdocbuilder.mapper.UserMapper;
import esg.esgdocbuilder.model.dto.response.UserProfileResponse;
import esg.esgdocbuilder.model.entity.User;
import esg.esgdocbuilder.repository.UserRepository;
import esg.esgdocbuilder.service.UserProfileService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Cloudinary cloudinary;

    @Override
    public UserProfileResponse getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserDoesNotExistsException(ApiErrorMessage.USER_DOES_NOT_EXIST.getMessage()));

        return userMapper.getUserProfile(user);
    }

    @Override
    public UserProfileResponse uploadUserPhoto(MultipartFile file) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElseThrow(
                    () -> new UserDoesNotExistsException(ApiErrorMessage.USER_DOES_NOT_EXIST.getMessage())
            );
            if (file == null || file.isEmpty()) {
                throw new FileIsEmptyException(ApiErrorMessage.FILE_IS_EMPTY.getMessage());
            }
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "user_photos")
            );
            String photoUrl = (String) uploadResult.get("secure_url");
            user.setImageUrl(photoUrl);
            userRepository.save(user);
            return userMapper.getUserProfile(user);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload photo", e);
        }

    }
}
