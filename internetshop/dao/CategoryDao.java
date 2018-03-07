package com.divanxan.internetshop.dao;

import com.divanxan.internetshop.dto.Category;

import java.util.List;

public interface CategoryDao {

    List<Category> list();
    Category get(int id);
    boolean add(Category category);
    boolean update(Category category);
    boolean delete(Category category);
    boolean setActive(Category category);
    boolean deleteForTest(Category category);
}
