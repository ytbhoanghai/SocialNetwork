package com.nguyenhai.demo.Controller;

import com.nguyenhai.demo.Entity.InfoUser;
import com.nguyenhai.demo.Form.PostForm;
import com.nguyenhai.demo.Response.BasicUserInfoResponse;
import com.nguyenhai.demo.Response.FriendRequestResponse;
import com.nguyenhai.demo.Response.NotificationResponse;
import com.nguyenhai.demo.Service.MeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/me")
public class MeController {

    private MeService meService;

    @Autowired
    public MeController(MeService meService) {
        this.meService = meService;
    }

    @GetMapping(value = "verify")
    public String verifyAccount(@RequestParam String code) {
        return meService.verifyAccount(code) ? "redirect:/" : "redirect:/error";
    }

    @GetMapping(value = "basic-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBasicInfoByMe(Principal principal) {
        String email = principal.getName();
        BasicUserInfoResponse response = meService.getBasicInfoByMe(email);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "info-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getInfoUser(Principal principal) {
        String email = principal.getName();
        InfoUser infoUser = meService.getInfoUserByEmail(email);
        return ResponseEntity.ok(infoUser);
    }

    @GetMapping(value = "/may-know", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBasicInfoOfOtherPeopleMayBeKnow(@RequestParam(value = "max-size", required = false, defaultValue = "30") Integer maxSize, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(meService.getBasicInfoOfOtherPeopleMayBeKnow(email, maxSize));
    }

    @GetMapping(value = "friend/basic-info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getListBasicFriendInfo(@RequestParam String term, Principal principal) {
        String email = principal.getName();
        List<BasicUserInfoResponse> responses = meService.getListBasicFriendInfoByTerm(term, email);
        return ResponseEntity.ok(responses);
    }

    @GetMapping(value = "friend/status", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> getFriendStatus(@RequestParam String id, Principal principal) {
        String email = principal.getName();
        String response = meService.getFriendStatus(id, email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "friend", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> unFriend(@RequestParam String id, Principal principal) {
        String email = principal.getName();
        meService.unFriend(id, email);
        return ResponseEntity.ok("unFriend with user " + id);
    }

    @GetMapping(value = "friend-request/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getNumberUnreadFriendRequest(Principal principal) {
        String email = principal.getName();
        HashMap<String, String> response = meService.getDetailsFriendRequest(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "friend-request/viewed", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> updateViewedFriendRequest(@RequestBody List<String> ids, Principal principal) {
        String email = principal.getName();
        long response = meService.updateNumberOfFriendRequestViewed(ids, email);
        return ResponseEntity.ok(String.valueOf(response));
    }

    @GetMapping(value = "friend-request/{page}/{limit}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFriendRequests(@PathVariable Integer page, Principal principal, @PathVariable Integer limit) {
        String email = principal.getName();
        List<FriendRequestResponse> response = meService.getFriendRequests(email, page, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "friend-request/next/{id}")
    public ResponseEntity<?> getNextFriendRequest(@PathVariable String id, @RequestParam Integer number, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(meService.getNextFriendRequest(email, id, number));
    }

    @PostMapping(value = "friend-request", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> addFriendRequest(@RequestParam String id, Principal principal) {
        String email = principal.getName();
        meService.addFriendRequest(id, email);
        return ResponseEntity.ok("friend request successfully, waiting for approval");
    }

    @DeleteMapping(value = "friend-request/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> deleteFriendRequest(@PathVariable String id, @RequestParam(required = false, defaultValue = "true") Boolean notify, Principal principal) {
        String email = principal.getName();
        meService.deleteFriendRequest(id, email, notify);
        return ResponseEntity.ok("\"friend request\" deleted successfully");
    }

    @PostMapping(value = "friend-request/accept", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> acceptFriendRequest(@RequestParam String id, @RequestParam(required = false, defaultValue = "true") Boolean notify, Principal principal) {
        String email = principal.getName();
        meService.acceptFriendRequest(id, email, notify);
        return  ResponseEntity.ok("accept friend request with user " + id);
    }

    @PostMapping(value = "following", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> addFollowing(@RequestParam String id, Principal principal) {
        String email = principal.getName();
        meService.addFollowing(id, email);
        return ResponseEntity.ok("successfully added user " + id + " to following list");
    }

    @DeleteMapping(value = "following", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> deleteFollowing(@RequestParam String id, Principal principal) {
        String email = principal.getName();
        meService.deleteFollowing(id, email);
        return ResponseEntity.ok("successfully deleted the user " + id + " to following list");
    }

    @PostMapping(value = "block", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> addToBlockList(@RequestParam String id, Principal principal) {
        String email = principal.getName();
        meService.addToBlockList(id, email);
        return ResponseEntity.ok("successfully added user " + id + " to block list");
    }

    @PutMapping(value = "un-block", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> unBlockUser(@RequestParam String id, Principal principal) {
        String email = principal.getName();
        meService.unBlockUserById(id, email);
        return ResponseEntity.ok("successfully removed user " + id + " from the block list");
    }

    @GetMapping(value = "history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getHistory(@RequestParam Integer page, @RequestParam Integer number, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(meService.getHistory(page, number, email));
    }

    @GetMapping(value = "history/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getHistoryNext(@PathVariable String id, @RequestParam Integer number, Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(meService.getHistoryNext(id, number, email));
    }
}
