package com.pinyougou.search.listener;

import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;

import javax.jms.*;
import java.util.List;

public class AddItemSolrMessageListener implements MessageListener {


    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 向索引库中添加索引
     * @param message
     */
    @Override
    public void onMessage(Message message) {
        try {
            //获取MQ中消息
            TextMessage textMessage= (TextMessage) message;
            String goodsId =textMessage.getText();

            //组装条件   上架的商品有不同 规格 所以 是一对多情况非 需要条件查询
            TbItemExample example=new TbItemExample();
            example.createCriteria().andGoodsIdEqualTo(Long.parseLong(goodsId));
            List<TbItem> itemList = tbItemMapper.selectByExample(example);

            solrTemplate.saveBeans(itemList);
            solrTemplate.commit();//增删稿都需要commit

        } catch (JMSException e) {
            e.printStackTrace();
        }



    }
}
