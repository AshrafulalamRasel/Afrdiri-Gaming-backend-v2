package com.afridi.gamingbackend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GameHistoryQuery {

    private String userId;

    private String f_key;

}
