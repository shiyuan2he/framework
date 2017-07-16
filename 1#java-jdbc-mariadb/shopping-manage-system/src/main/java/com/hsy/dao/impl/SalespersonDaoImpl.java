package com.hsy.dao.impl;

import com.hsy.dao.ISalespersonDao;
import com.hsy.entity.Salesperson;
import com.hsy.util.DBUtils;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author heshiyuan
 * @description <p></p>
 * @path framework/com.hsy.dao.impl
 * @date 2017/7/15 下午5:59
 * @github http://github.com/shiyuan2he
 * @email shiyuan4work@sina.com
 * Copyright (c) 2017 shiyuan4work@sina.com All rights reserved.
 * @price ¥5    微信：hewei1109
 */
public class SalespersonDaoImpl extends BaseDaoImpl implements ISalespersonDao {

    public ArrayList<Salesperson> queryAllSalespersonList() {
        ArrayList<Salesperson> salesManList = new ArrayList<Salesperson>();
        conn = DBUtils.getConnetction();
        String sql = "SELECT * FROM t_salesperson";

        try {
            pstmt = conn.prepareStatement(sql);
            rs =  pstmt.executeQuery();
            while (rs.next()) {
                Long sId = rs.getLong(1);
                String name = rs.getString(2);
                String password = rs.getString(3);

                Salesperson salesMan = new Salesperson(sId,name,password);
                salesManList.add(salesMan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtils.closeResource(pstmt, rs, conn);
        }
        return salesManList;
    }
}