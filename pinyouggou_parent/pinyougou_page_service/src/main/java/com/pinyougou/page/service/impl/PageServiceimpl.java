package com.pinyougou.page.service.impl;

import GroupEntity.Goods;
import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.PageService;
import com.pinyougou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class PageServiceimpl  implements PageService {

    @Autowired
    private TbItemMapper tbItemMapper; //商品类目表
    @Autowired
    private TbGoodsMapper tbGoodsMapper; //商品表
    @Autowired
    private TbGoodsDescMapper tbGoodsDescMapper; //商品描述表
    @Autowired
    private TbItemCatMapper tbItemCatMapper;// 商品类目分类表

    @Override
    public Goods findOne(Long goodsId) {
        Goods goods=new Goods();

        TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(goodsId);
        goods.setGoods(tbGoods);

        String category1Name = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory1Id()).getName();
        String category2Name = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory2Id()).getName();
        String category3Name = tbItemCatMapper.selectByPrimaryKey(tbGoods.getCategory3Id()).getName();

        Map<String,String> categoryMap=new HashMap<>();

        categoryMap.put("category1Name",category1Name);
        categoryMap.put("category2Name",category2Name);
        categoryMap.put("category3Name",category3Name);

        goods.setCategoryMap(categoryMap);



        TbGoodsDesc tbGoodsDesc = tbGoodsDescMapper.selectByPrimaryKey(goodsId);
        goods.setGoodsDesc(tbGoodsDesc);


        TbItemExample example=new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        List<TbItem> itemList = tbItemMapper.selectByExample(example);
        goods.setItemList(itemList);

    return goods;

    }
}
