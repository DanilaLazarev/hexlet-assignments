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
    public ResponseEntity<List<Post>> showPosts(@RequestParam String id){
        var filteredPosts = posts.stream().filter(x->x.getUserId()==Integer.parseInt(id)).toList();
        return ResponseEntity.ok().body(filteredPosts);
    }

    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPost(@RequestParam String id,@RequestBody Post post){
        var newPost = new Post();
        newPost.setUserId(Integer.parseInt(id));
        newPost.setTitle(post.getTitle());
        newPost.setBody(post.getBody());
        newPost.setSlug(post.getSlug());
        return ResponseEntity.status(HttpStatus.CREATED).body(newPost);
    }
}

// END
