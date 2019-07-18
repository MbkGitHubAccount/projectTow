package com.pinyougou.search.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class DeleItemSolrMessageListener implements MessageListener {


    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 从索引库删除 响应的索引
     * @param message
     */
    @Override
    public void onMessage(Message message) {

        try {
            TextMessage textMessage= (TextMessage) message;
            String goodsId =textMessage.getText();
            //这里需要条件查询   在商品 中有以一个字段是 （item_goodsid）
            SolrDataQuery query=new SimpleQuery("item_goodsid:"+goodsId);
            solrTemplate.delete(query);
            solrTemplate.commit();
        } catch (JMSException e) {
            e.printStackTrace();
        }







    }
}
