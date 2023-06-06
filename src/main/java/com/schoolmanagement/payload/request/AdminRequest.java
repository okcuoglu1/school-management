package com.schoolmanagement.payload.request;

import com.schoolmanagement.payload.request.abstracts.BaseUserRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AdminRequest extends BaseUserRequest {

    private boolean built_in; //Herhangi bir obje built_in ise service katında silemezsin diye belirticez.




}
