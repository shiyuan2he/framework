package com.hsy.dao.impl;

import com.hsy.dao.IGoodsDao;
import com.hsy.entity.Goods;
import com.hsy.util.Constant;
import com.hsy.util.DateUtils;
import com.hsy.util.MathUtils;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author heshiyuan
 * @description <p>商品数据访问层</p>
 * @date 2017/7/21 14:37
 * @email shiyuan4work@sina.com
 * @github https://github.com/shiyuan2he.git
 * Copyright (c) 2016 shiyuan4work@sina.com All rights reserved
 */
@Repository("goodsDao")
public class GoodsDaoImpl extends BaseDaoImpl<Goods> implements IGoodsDao {
    @Override
    public boolean save(Goods goods) {
        final String sql = "insert into t_goods(id,name,price,number,state," +
                "create_time,create_user) values(?,?,?,?,?,?,?)";
        int count = jdbcTemplate.update(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, MathUtils.generateRandomByLength(8));
                ps.setString(2, goods.getName());
                ps.setDouble(3, goods.getPrice());
                ps.setInt(4, goods.getNumber());
                ps.setInt(5,0);// 默认状态
                ps.setTimestamp(6, DateUtils.getCurrentTimestamp());
                ps.setInt(7,49972586);
            }
        });
        if(count>0){
            return true ;
        }
        return false;
    }

    @Override
    public boolean saveOtherWay(Goods goods) {
        final String sql = "insert into t_goods(id,name,price,number,state," +
                "create_time,create_user) values(?,?,?,?,?,?,?)";
        int count = jdbcTemplate.update(new PreparedStatementCreator(){
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps=con.prepareStatement(sql);
                ps.setInt(1, MathUtils.generateRandomByLength(8));
                ps.setString(2, goods.getName());
                ps.setDouble(3, goods.getPrice());
                ps.setInt(4, goods.getNumber());
                ps.setInt(5,0);// 默认状态
                ps.setTimestamp(6, DateUtils.getCurrentTimestamp());
                ps.setInt(7,49972586);
                return ps;
            }
        });
        if(count>0){
            return true ;
        }
        return false;
    }

    @Override
    public Integer batchSave(List<Goods> list) {
        Integer count = 0 ;
        for(Goods goods : list){
            if(save(goods)){
                count ++ ;
            }
        }
        return count;
    }

    @Override
    public boolean deleteById(Integer id) {
        Assert.isNull(id,"id can not be null!");
        // 定义返回结果
        boolean flag = false;
        /* 插入实现 */
        int i = jdbcTemplate.update("delete from t_goods where id=? ", new Object[] { id });
        if (i > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean delete(Goods goods) {
        return deleteById(goods.getId());
    }

    @Override
    public Integer batchDelete(List<Goods> list) {
        return null;
    }
    @Override
    public boolean update(Goods goods) {
        return false;
    }

    @Override
    public Integer batchUpdate(List<Goods> list) {
        return null;
    }

    @Override
    public List<Goods> selectList(Integer currentPage,Integer pageSize) {
        if(currentPage <= 1) currentPage = 1;
        StringBuilder sql = new StringBuilder("select id,name,price,number from t_goods") ;
        long countAll = Long.parseLong(String.valueOf(super.jdbcTemplate.queryForList("select count(ccc.id) as countAll from ("+sql.toString()+") ccc").get(0).get("countAll"))) ;
        Constant.requestThreadLocal.get().setAttribute("totalCount",countAll);
        if(currentPage>0&&pageSize>0){
            sql.append(" limit ").append((currentPage-1) * pageSize).append(" , ").append(pageSize) ;
        }
        List<Goods> goodsList = new ArrayList<>();
        super.jdbcTemplate.query(sql.toString(),new RowCallbackHandler(){
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                goodsList.add(new Goods(rs.getInt("id"),rs.getString("name"),rs.getDouble("price"),rs.getInt("number"))) ;
            }
        });
        return goodsList;
    }

    @Override
    public List<Goods> selectList(Goods goods, Integer currentPage, Integer pageSize) {
       /* String sql = "select id,name,price,number from t_goods where id = ?" ;
        RowMapper<Goods> rowMapper = new BeanPropertyRowMapper<>(Goods.class);
        super.jdbcTemplate.query()*/
        return null ;
    }

    @Override
    public Goods getById(Integer id) {
        String sql = "select id,name,price,number from t_goods where id = ?" ;
        RowMapper<Goods> rowMapper = new BeanPropertyRowMapper<>(Goods.class);
        return super.jdbcTemplate.queryForObject(sql,rowMapper,id) ;
    }
}
