package ra.project_module04.service;

import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.FormLogin;
import ra.project_module04.model.dto.req.FormRegister;
import ra.project_module04.model.dto.resp.JwtResponse;

public interface IAuthService {
    boolean register(FormRegister formRegister) throws CustomException;

    JwtResponse login(FormLogin formLogin) throws  CustomException;

}
