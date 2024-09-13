package exercise.controller.users;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import exercise.model.Post;
import exercise.Data;

// BEGIN
@RestController
@RequestMapping("/api")
public class PostsController {

    private List<Post> posts = Data.getPosts();

    @GetMapping("/users/{id}/posts")
    public ResponseEntity<List<Post>> showPosts(@PathVariable("id") int id){
        var filteredPosts = posts.stream().filter(x->x.getUserId()==id).toList();
        return ResponseEntity.ok().body(filteredPosts);
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable("id") int id,@RequestBody Post post){
        var newPost = new Post();
        newPost.setUserId(id);
        newPost.setTitle(post.getTitle());
        newPost.setBody(post.getBody());
        newPost.setSlug(post.getSlug());
        return ResponseEntity.status(HttpStatus.CREATED).body(newPost);
    }
}

// END
