package com.hops.hops_new_api.common.model.Response;

import com.hops.hops_new_api.common.model.data.CustomerMapDto;
import com.hops.hops_new_api.common.model.data.UserDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserLoginResponse {

    private List<CustomerMapDto> loginMapList = new ArrayList<>();
    private  UserDto userDto = new UserDto();
    private List<UserDto> userDtoList = new ArrayList<>();

}
