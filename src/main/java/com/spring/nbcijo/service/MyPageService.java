package com.spring.nbcijo.service;

import com.spring.nbcijo.dto.request.UpdateDescriptionRequestDto;
import com.spring.nbcijo.dto.response.MyInformResponseDto;
import com.spring.nbcijo.dto.response.PostResponseDto;
import com.spring.nbcijo.entity.Post;
import com.spring.nbcijo.entity.User;
import com.spring.nbcijo.global.enumeration.ErrorCode;
import com.spring.nbcijo.global.exception.InvalidInputException;
import com.spring.nbcijo.repository.PostRepository;
import com.spring.nbcijo.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    public MyInformResponseDto getMyInform(User user) {
        user = userRepository.findById(user.getId())
            .orElseThrow(() -> new InvalidInputException(ErrorCode.USER_NOT_FOUND));
        return MyInformResponseDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .role(user.getRole())
            .description(user.getDescription())
            .build();
    }

    @Transactional
    public void updateMyDescription(User user,
        UpdateDescriptionRequestDto updateDescriptionRequestDto) {
        user = userRepository.findById(user.getId())
            .orElseThrow(() -> new InvalidInputException(ErrorCode.USER_NOT_FOUND));
        if (passwordEncoder.matches(updateDescriptionRequestDto.getPassword(),
            user.getPassword())) {
            user.updateDescription(user, updateDescriptionRequestDto);
        } else {
            throw new InvalidInputException(ErrorCode.INVALID_PASSWORD);
        }
    }

    public List<PostResponseDto> getMyPosts(User user) {
        List<Post> postList = postRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
        List<PostResponseDto> postListToDtos = postList.stream().map(PostResponseDto::new).collect(
            Collectors.toList());
        return postListToDtos;
    }
}
