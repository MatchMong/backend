/*
package com.example.demo.write.service;

import org.springframework.stereotype.Service;
import com.example.demo.write.entity.Writer;
import com.example.demo.write.repository.WriteRepository;

@Service
public class Writeservice {
    private final WriteRepository writeRepository;

    public Writeservice(WriteRepository writeRepository) {
        this.writeRepository = writeRepository;
    }

    public Writer update(Long id, Writer updatewriter) {
        Writer outWriter = writeRepository.findById(id).orElseThrow(()-> new RuntimeException("글을 찾을 수 없습니다."));

        outWriter.setTitle(updatewriter.getTitle());
        outWriter.setContent(updatewriter.getContent());

        return writeRepository.save(outWriter);
    }

    public Writer writeview(Long id) {
        return writeRepository.findById(id).orElseThrow(()-> new RuntimeException("글을 찾을 수 없습니다."));
    }

    public Writer insert(Writer insertwriterm) {
        return writeRepository.save(insertwriterm);
    }
    public void delete(Long id) {
        writeRepository.deleteById(id);
    }

}
*/
