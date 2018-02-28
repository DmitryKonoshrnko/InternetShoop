package com.divanxan.internetshop.controller;


import com.divanxan.internetshop.dao.UserDao;
import com.divanxan.internetshop.dto.Cart;
import com.divanxan.internetshop.dto.CartLine;
import com.divanxan.internetshop.dto.User;
import com.divanxan.internetshop.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.util.List;


//данный контроллер будет участвовать в каждом запросе. Существует для отображения информации о юзере. использует UserModel

@ControllerAdvice
public class GlobalController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private HttpSession session;

    @ModelAttribute("userModel")
    public UserModel getUserModel() {
        UserModel userModel = ((UserModel) session.getAttribute("userModel"));
        if (userModel == null || userModel.getEmail()==null) {
            //добавим покупателя
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            User user = userDao.getByEmail(authentication.getName());

            if(user!=null){
                //создаем модель покупателя для отображения покупателя на странице
                userModel = new UserModel();

                userModel.setId(user.getId());
                userModel.setEmail(user.getEmail());
                userModel.setRole(user.getRole());
                userModel.setFullName(user.getFirstName() + " " + user.getLastName());

                //если что, то надо убрать ето
                userModel.setCart(user.getCart());
          // TODO если что - посмотри тут. с корзиной надо разобраться
                if(userModel.getRole().equals("USER")){
                    //добавляем корзину только в случае если юзер - покупатель
                    userModel.setCart(user.getCart());
                }

                //добавляем модель покупателя в сессию
                session.setAttribute("userModel", userModel);
                return userModel;
            }
            else{
                // если юзер аноним, то создаем корзину
                userModel = new UserModel();
                Cart cart = new Cart();
                userModel.setCart(cart);
                List<CartLine> cartLines = null;
                //добавляем модель покупателя в сессию
                session.setAttribute("userModel", userModel);
                session.setAttribute("AnonymousCartLines", cartLines);
                return userModel;
            }
        }

        return (UserModel) session.getAttribute("userModel");
    }

}
