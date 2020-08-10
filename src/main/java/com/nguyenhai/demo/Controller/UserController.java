package com.nguyenhai.demo.Controller;

import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Response.BasicUserInfoResponse;
import com.nguyenhai.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String requestPageUser(@RequestParam String id, Principal principal) {
        String email = principal.getName();
        try {
            if (userService.isAccept(id, email)) {
                return "user";
            }
        } catch (RuntimeException ignore) { }
        return "redirect:/error";
    }

    @GetMapping(value = "id", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> getIdByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.findIdByEmail(email));
    }

    @GetMapping(value = "basic-info/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBasicUserInfo(@PathVariable String id, Principal principal) {
        String email = principal.getName();;
        BasicUserInfoResponse response = userService.getBasicInfoById(id, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "info-user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getInfoUser(@PathVariable String id, Principal principal) {
        String email = principal.getName();
        InfoUser response = userService.getInfoUserById(id, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "friends/{id}/{page}/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListFriend(@PathVariable String id,
                                           @PathVariable Integer page,
                                           @PathVariable Integer number,
                                           @RequestParam(required = false, defaultValue = "all-friends") String option,
                                           Principal principal) {

        String email = principal.getName();

        switch (option) {
            case "recently-added":
                return ResponseEntity.ok(userService.getListFriendsRecentlyAdded(email, id, page, number));
            case "block":
                return ResponseEntity.ok(userService.getListFriendsBlocked(email, id, page, number));
            case "hometown":
                return ResponseEntity.ok(userService.getListFriendsSameHometown(email, id, page, number));
            case "following":
                return ResponseEntity.ok(userService.getListFollowing(email, id, page, number));
            case "online":
                return ResponseEntity.ok(userService.getListUserOnline(id, page, number));
        }
        return ResponseEntity.ok(userService.getListFriends(email, id, page, number));
    }

    @GetMapping(value = "{id}/post", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPost(@PathVariable String id, @RequestParam Integer page, @RequestParam Integer number, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(userService.getPost(id, page, number, email));
    }

    @GetMapping(value = "{idUser}/post/{idPost}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getNextPost(@PathVariable String idUser, @PathVariable String idPost, @RequestParam Integer number, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(userService.getNextPost(idUser, idPost, number, email));
    }

    @GetMapping(value = "{idUser}/timeline", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTimeline(@PathVariable String idUser, @RequestParam(required = false, defaultValue = "-1") String idPostStart, @RequestParam Integer number, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(userService.getTimeline(idUser, idPostStart, number, email));
    }

    @GetMapping(value = "{idUser}/photos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPhotos(@PathVariable String idUser, @RequestParam Integer page, @RequestParam Integer number) {
        return ResponseEntity.ok(userService.getPhotos(idUser, page, number));
    }

    @GetMapping(value = "{idUser}/friends/UpComingBirthday", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListUpcomingBirthdayOfFriend(@PathVariable String idUser) {
        return ResponseEntity.ok(userService.getListUpcomingBirthdayOfFriend(idUser));
    }
}
