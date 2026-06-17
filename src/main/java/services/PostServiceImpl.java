package services;

import DTO.PostDTO;

import java.util.List;

public interface PostServiceImpl {

    List<PostDTO> getAllPosts();

    PostDTO createNewPost(PostDTO inputPost);

    PostDTO getPostById(Long postId);
}