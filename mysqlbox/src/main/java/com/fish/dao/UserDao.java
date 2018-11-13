package com.fish.dao;

import com.fish.entity.User;

/**
 * @author yu_liu6@hnair.com
 * @date $date$
 */
public interface UserDao {
    User selectUser(long id);
}
