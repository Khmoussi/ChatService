package com.example.messenger.POJO;

import com.example.messenger.POJO.Enums.FileType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity

public class FileMessage extends Message   {

    private String fileContent;
    private FileType fileType;

}
