package com.schoolmanagement.payload.response;

import com.schoolmanagement.payload.request.abstracts.BaseUserRequest;
import com.schoolmanagement.payload.response.abstracts.BaseUserResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AdminResponse extends BaseUserResponse { //response icin kullancağım dto
}
