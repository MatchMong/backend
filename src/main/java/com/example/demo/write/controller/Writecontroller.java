//package com.example.demo.write.controller;
//
//import com.example.demo.write.entity.room;
//import com.example.demo.write.service.Writeservice;
//import org.springframework.web.bind.annotation.*;
//import com.example.demo.discordbot.ROOM.*;
//import com.example.demo.discordbot.DiscordBot;
//
//@CrossOrigin(
//        origins = "*",
//        allowedHeaders = "*",
//        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
//)
//
//@RestController
//public class Writecontroller {
//    private final Writeservice writeservice;
//
//    public Writecontroller(Writeservice writeservice) {
//        this.writeservice = writeservice;
//    }
//
//    @PutMapping("/write/change/{id}")
//    public room rewrite(@PathVariable Long id, @RequestBody room updatewriter) {
//        return writeservice.update(id, updateroom);
//    }
//
//    @GetMapping("/wrtie/out/{id}")
//    public room out(@PathVariable Long id) {
//        return writeservice.writeview(id);
//    }
//
//    @PostMapping("/write/create")
//    public room insert(@RequestBody Writer insertwriter) {
//        return writeservice.insert(insertwriter);
//    }
//
//    @DeleteMapping("/write/delete/{id}")
//    public String delete(@PathVariable Long id) {
//        writeservice.delete(id);
//        return "삭제 되었습니다.";
//    }
//
//}
