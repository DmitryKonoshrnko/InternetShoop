package com.divanxan.internetshop.daoimpl;

import com.divanxan.internetshop.dao.UserDao;
import com.divanxan.internetshop.dto.Address;
import com.divanxan.internetshop.dto.OrderDetail;
import com.divanxan.internetshop.dto.Product;
import com.divanxan.internetshop.dto.User;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Repository("userDao")
@Transactional
public class UserDaoImpl implements UserDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean addUser(User user) {
        try {
            sessionFactory.getCurrentSession().persist(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(User user) {
        try {
            sessionFactory.getCurrentSession().update(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delleteForTestUser(User user) {
        try {
            sessionFactory.getCurrentSession().delete(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public User getByEmail(String email) {
        if (email.equals("anonymousUser")) return null;
        try {
        Query query = sessionFactory.getCurrentSession()
                .getNamedQuery("userGetByEmail")
                .setParameter("email", email);

        return (User) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }


//        String selectQuery = "FROM User WHERE email =:email";
//        try {
//            return sessionFactory.getCurrentSession()
//                    .createQuery(selectQuery, User.class)
//                    .setParameter("email", email)
//                    .getSingleResult();
//        } catch (Exception e) {
//            // e.printStackTrace();
//            return null;
//        }
    }

    @Override
    public User getById(int id) {

        Query query = sessionFactory.getCurrentSession()
                .getNamedQuery("userGetById")
                .setParameter("id", id);

        return (User) query.getSingleResult();

//        String selectQuery = "FROM User WHERE id =:id";
//        return sessionFactory.getCurrentSession()
//                .createQuery(selectQuery, User.class)
//                .setParameter("id", id)
//                .getSingleResult();
    }

    @Override
    public boolean addAddress(Address address) {
        try {
            sessionFactory.getCurrentSession().persist(address);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Address getAddress(int addressId) {
        try {
            return sessionFactory.getCurrentSession().get(Address.class, addressId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public boolean updateAddress(Address address) {
        try {
            sessionFactory.getCurrentSession().update(address);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Address getBillingAddress(int userId) {
        Query query = sessionFactory.getCurrentSession()
                .getNamedQuery("getBillingAddress")
                .setParameter("userId", userId)
                .setParameter("billing", true);
        Address address = (Address) query.getSingleResult();
        return address;

//        String selectQuery = "FROM Address WHERE userId =:userId AND billing =:billing";
//        try {
//            return sessionFactory.getCurrentSession()
//                    .createQuery(selectQuery, Address.class)
//                    .setParameter("userId", userId)
//                    .setParameter("billing", true)
//                    .getSingleResult();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    @Override
    public List<Address> listShippingAddressess(int userId) {
        Query query = sessionFactory.getCurrentSession()
                .getNamedQuery("listShippingAddresses")
                .setParameter("userId", userId)
                .setParameter("shipping", true);

        return query.getResultList();

//        String selectQuery = "FROM Address WHERE userId =:userId AND shipping =:shipping";
//        try {
//            return sessionFactory.getCurrentSession()
//                    .createQuery(selectQuery, Address.class)
//                    .setParameter("userId", userId)
//                    .setParameter("shipping", true)
//                    .getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    @Override
    public List<Address> listAddressess(int userId) {

        Query query = sessionFactory.getCurrentSession()
                .getNamedQuery("listAddresses")
                .setParameter("userId", userId);

        return query.getResultList();

//        String selectQuery = "FROM Address WHERE userId =:userId";
//        try {
//            return sessionFactory.getCurrentSession()
//                    .createQuery(selectQuery, Address.class)
//                    .setParameter("userId", userId)
//                    .getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    @Override
    public List<OrderDetail> listOrders(int userId) {
        Query query = sessionFactory.getCurrentSession()
                .getNamedQuery("listOrders")
                .setParameter("userId", userId);

        return query.getResultList();

//        String selectQuery = "FROM OrderDetail WHERE user.id =:userId";
//        try {
//            return sessionFactory.getCurrentSession()
//                    .createQuery(selectQuery, OrderDetail.class)
//                    .setParameter("userId", userId)
//                    .getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    @Override
    public List<OrderDetail> listAllOrders() {
        Query query = sessionFactory.getCurrentSession()
                .getNamedQuery("listAllOrders");
        return query.getResultList();

//        String selectQuery = "FROM OrderDetail";
//        try {
//            return sessionFactory.getCurrentSession()
//                    .createQuery(selectQuery, OrderDetail.class)
//                    .getResultList();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
    }

    @Override
    public List<OrderDetail> listThisMonthOrders() {

        Calendar date1 = Calendar.getInstance();   // this takes current date
        date1.set(Calendar.DAY_OF_MONTH, 1);
        date1.set(Calendar.HOUR_OF_DAY, 0);
        date1.set(Calendar.MINUTE, 0);
        date1.set(Calendar.SECOND, 0);
        Calendar date2 = (Calendar) date1.clone();
        date2.add(Calendar.MONTH, 1);

        Query query = sessionFactory.getCurrentSession()
                .getNamedQuery("listDateOrders")
                .setParameter("date1", date1.getTime())
                .setParameter("date2", date2.getTime());

        return query.getResultList();

//        String selectQuery = "FROM OrderDetail WHERE orderDate BETWEEN :date1 AND :date2";
//            return sessionFactory.getCurrentSession()
//                    .createQuery(selectQuery, OrderDetail.class)
//                    .setParameter("date1", date1.getTime())
//                    .setParameter("date2", date2.getTime())
//                    .getResultList();

    }

    @Override
    public List<Product> getTopProducts() {
        Query query = sessionFactory.getCurrentSession()
                .getNamedQuery("getTopProducts")
                .setParameter("active",true)
                .setMaxResults(10);
        return query.getResultList();

//        String selectQuery = "FROM Product ORDER BY purchases desc ";
//
//        List<Product> list = sessionFactory.getCurrentSession()
//                .createQuery(selectQuery, Product.class)
//                .setMaxResults(10)
//                .getResultList();
//        return list;
    }

    @Override
    public List<OrderDetail> listThisWeekOrders() {

        Calendar date1 = Calendar.getInstance();   // this takes current date
        date1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        date1.set(Calendar.HOUR_OF_DAY, 0);
        date1.set(Calendar.MINUTE, 0);
        date1.set(Calendar.SECOND, 0);
        Calendar date2 = (Calendar) date1.clone();
        date2.add(Calendar.DAY_OF_WEEK, 7);
        System.out.println(date1.getTime());
        System.out.println(date2.getTime());
        Query query = sessionFactory.getCurrentSession()
                .getNamedQuery("listDateOrders")
                .setParameter("date1", date1.getTime())
                .setParameter("date2", date2.getTime());


        List<OrderDetail> jj = query.getResultList();
        System.out.println("");
        return query.getResultList();

//        String selectQuery = "FROM OrderDetail WHERE orderDate BETWEEN :date1 AND :date2";
//        return sessionFactory.getCurrentSession()
//                .createQuery(selectQuery, OrderDetail.class)
//                .setParameter("date1", date1.getTime())
//                .setParameter("date2", date2.getTime())
//                .getResultList();
    }

}
