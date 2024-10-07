package com.docsachdi.controllers;

import com.docsachdi.request.ListUserBookReq;
import com.docsachdi.request.UpdateStatusBookReq;
import com.docsachdi.response.ListUserBookRes;
import com.docsachdi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/update-status-book")
    public ResponseEntity<String> updateStatusBook(@RequestBody UpdateStatusBookReq req) {
       boolean res = userService.updateStatusBook(req);
        if (res) {
            return ResponseEntity.ok("Update status book successfully");
        } else {
            return ResponseEntity.status(400).body("Failed to update status book");
        }
    }

    @GetMapping("/get-user-books")
    public ResponseEntity<ListUserBookRes> updateStatusBook(@RequestBody ListUserBookReq req) {
        ListUserBookRes res = userService.getListUserBook(req);
        if(res==null)
            return ResponseEntity.status(400).body(null);
        return ResponseEntity.ok(res);
    }
}
