package com.divanxan.internetshop.service;

import com.divanxan.internetshop.dao.CartLineDao;
import com.divanxan.internetshop.dao.ProductDao;
import com.divanxan.internetshop.dto.Cart;
import com.divanxan.internetshop.dto.CartLine;
import com.divanxan.internetshop.dto.Product;
import com.divanxan.internetshop.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service("cartService")
public class CartService {

    @Autowired
    private CartLineDao cartLineDao;

    @Autowired
    private HttpSession session;

    @Autowired
    private ProductDao productDao;


    // возвращает корзину зарегестрированого покупателя
    private Cart getCart() {
        Cart cart = null;
        try {
            cart = ((UserModel) session.getAttribute("userModel")).getCart();
        } catch (Exception e) {
            UserModel userModel = (UserModel) session.getAttribute("userModel");
            cart = new Cart();
            userModel.setCart(cart);
            session.setAttribute("userModel", userModel);
        }
        return cart;
    }

    private List<CartLine> getCartLinesList() {
        List<CartLine> list = (List<CartLine>) session.getAttribute("AnonymousCartLines");
        return list;
    }

    //возвращает содержимое корзины
    public List<CartLine> getCartLines() {
        Cart cart = this.getCart();
        List<CartLine> cartLines = null;
        if (cart.getUser() != null) {
            cartLines = cartLineDao.list(cart.getId());
        } else {
            cartLines = this.getCartLinesList();
        }
        return cartLines;
    }

    // обновим количество товара в корзине (используется в методе updateCart в CartController)
    public String updateCartLine(int cartLineId, int count) {
        Cart cart = this.getCart();
        CartLine cartLine = null;
        // если пользователь зарегестирован
        if (cart.getUser() != null) {
            // получим строку корзины
            cartLine = cartLineDao.get(cartLineId);
        }
        // если пользователь аноним
        else {
            cartLine = this.getCartLines().get(cartLineId);
        }

        if (cartLine == null) {
            return "result=error";
        } else {
            Product product = cartLine.getProduct();

            double oldTotal = cartLine.getTotal();

            // если покупатель хочет взять больше товара, чем у нас есть в наличии
            if (product.getQuantity() <= count) {
                count = product.getQuantity();
            }
            cartLine.setProductCount(count);
            cartLine.setBuyingPrice(product.getUnitPrice());
            cartLine.setTotal(product.getUnitPrice() * count);
            // если пользователь зарегестирован
            if (cart.getUser() != null) {
                //обновляем сроку в корзине
                cartLineDao.update(cartLine);
            }

            // обновляем общую стоимость покупки
            cart.setGrandTotal(cart.getGrandTotal() - oldTotal + cartLine.getTotal());
            // обновляем корзину в бд если пользователь зарегестирован
            if (cart.getUser() != null) {
                cartLineDao.updateCart(cart);
            }
            return "result=update";
        }

    }


    // удалим товар из корзины (используется в методе deleteCart в CartController)
    public String deleteCartLine(int cartLineId) {
        // получим строку корзины
        Cart cart = this.getCart();
        if (cart.getUser() != null) {
            //если пользователь зарегестрирован
            CartLine cartLine = cartLineDao.get(cartLineId);
            if (cartLine == null) {
                return "result=error";
            } else {
                // удалим стоимость данной позиции
                cart.setGrandTotal(cart.getGrandTotal() - cartLine.getTotal());
                cart.setCartLines(cart.getCartLines() - 1);
                cartLineDao.updateCart(cart);
                // удалим позицию
                cartLineDao.delete(cartLine);

                return "result=deleted";
            }
        } else {
            List<CartLine> list = this.getCartLines();
            CartLine cartLine = list.get(cartLineId);
            if (cartLine == null) {
                return "result=error";
            } else {
                cart.setGrandTotal(cart.getGrandTotal() - cartLine.getTotal());
                cart.setCartLines(cart.getCartLines() - 1);
                list.remove(cartLineId);
                session.setAttribute("AnonymousCartLines", list);
                return "result=deleted";
            }
        }
    }

    // добавим товар в корзину (используется в методе addCart в CartController)
    public String addCartLine(int productId) {
        String response = null;

        Cart cart = this.getCart();
        CartLine cartLine = null;

        try {
            cartLine = cartLineDao.getByCartAndProduct(cart.getId(), productId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cartLine == null) {
            //добавим новую позицию
            cartLine = new CartLine();
            //найдем нужный товар
            Product product = productDao.get(productId);

            cartLine.setCartId(cart.getId());

            cartLine.setProduct(product);
            cartLine.setBuyingPrice(product.getUnitPrice());
            cartLine.setProductCount(1);

            cartLine.setTotal(product.getUnitPrice());
            cartLine.setAvailable(true);
            //если пользователь - зарегестрирован
            if (cart.getUser() != null) {
                cartLineDao.add(cartLine);
            } else {
                //если пользователь - Аноним
                List<CartLine> cartLines = this.getCartLines();
                if (cartLines != null) {
                    cartLines.add(cartLine);
                } else {
                    cartLines = new ArrayList<>();
                    cartLines.add(cartLine);
                    session.setAttribute("AnonymousCartLines", cartLines);
                }
            }
            cart.setCartLines(cart.getCartLines() + 1);
            cart.setGrandTotal(cart.getGrandTotal() + cartLine.getTotal());
            //если пользователь - зарегестрирован внесем изменения в БД
            if (cart.getUser() != null) {
                cartLineDao.updateCart(cart);
            }
            response = "result=added";
        }

        return response;
    }
}