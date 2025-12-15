package com.scu.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeleteBatchDTO implements Serializable {
    private Long[] ids;
}
