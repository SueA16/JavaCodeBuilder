//package com.cqnu;
//
//import com.cqnu.entity.po.ProductInfo;
//import com.cqnu.entity.query.ProductInfoQuery;
//import com.cqnu.mapper.ProductInfoMapper;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@SpringBootTest
//class MapperTest {
//
//    @Resource
//    private ProductInfoMapper<ProductInfo, ProductInfoQuery> productInfoMapper;
//
//    @Test
//    void selectList() {
//        ProductInfoQuery query = new ProductInfoQuery();
////        query.setId(2);
////        query.setCreateDateStart("2013-08-22");
//        query.setCodeFuzzy("88");
//        List<ProductInfo> productInfos = productInfoMapper.selectList(query);
//        for (ProductInfo p : productInfos) {
//            System.out.println(p);
//        }
//    }
//
//    @Test
//    void insertList() {
//        ProductInfo productInfo = new ProductInfo();
//        productInfo.setCode("10007");
//        productInfo.setSkuType(6);
//        productInfo.setColorType(1);
//        productInfo.setCreateTime(new Date());
//        productInfo.setCreateDate(new Date());
//        productInfoMapper.insert(productInfo);
//        System.out.println(productInfo.getId());
//    }
//
//    @Test
//    void insertOrUpdate() {
//        ProductInfo productInfo = new ProductInfo();
//        productInfo.setId(140);
//        productInfo.setCode("10009");
//        productInfo.setProductName("ccc");
//        productInfoMapper.insertOrUpdate(productInfo);
//    }
//
//    @Test
//    void insertBatch() {
//        List<ProductInfo> productInfos = new ArrayList<>();
//
//        ProductInfo productInfo = new ProductInfo();
//        productInfo.setCode("1111");
//        productInfo.setProductName("abc");
//        productInfo.setCreateDate(new Date());
//        productInfos.add(productInfo);
//
//        ProductInfo productInfo2 = new ProductInfo();
//        productInfo2.setCode("2222");
//        productInfo2.setProductName("edf");
//        productInfo2.setCreateDate(new Date());
//        productInfos.add(productInfo2);
//
//        productInfoMapper.insertBatch(productInfos);
//    }
//
//    @Test
//    void insertOrUpdateBatch() {
//        List<ProductInfo> productInfos = new ArrayList<>();
//
//        ProductInfo productInfo = new ProductInfo();
//        productInfo.setCode("1111");
//        productInfo.setProductName("1111");
//        productInfo.setCreateDate(new Date());
//        productInfos.add(productInfo);
//
//        ProductInfo productInfo2 = new ProductInfo();
//        productInfo2.setCode("2222");
//        productInfo2.setProductName("2222");
//        productInfo2.setCreateDate(new Date());
//        productInfos.add(productInfo2);
//
//        productInfoMapper.insertOrUpdateBatch(productInfos);
//    }
//
//    @Test
//    void selectByKey() {
//        ProductInfo productInfo = productInfoMapper.selectById(1);
//        System.out.println(productInfo);
//        ProductInfo productInfo2 = productInfoMapper.selectByCode("jaYaX2xEhk");
//        System.out.println(productInfo2);
//        ProductInfo productInfo3 = productInfoMapper.selectBySkuTypeAndColorType(5,1);
//        System.out.println(productInfo3);
//    }
//
//    @Test
//    void updateByKey() {
//        ProductInfo productInfo = new ProductInfo();
//        productInfo.setProductName("update by id 1");
//        productInfoMapper.updateById(productInfo,1);
//
//        ProductInfo productInfo2 = new ProductInfo();
//        productInfo2.setProductName("update by code 1111");
//        productInfoMapper.updateByCode(productInfo2,"1111");
//
//        ProductInfo productInfo3 = new ProductInfo();
//        productInfo3.setProductName("update by 6 and 1");
//        productInfoMapper.updateBySkuTypeAndColorType(productInfo3,6,1);
//    }
//
//    @Test
//    void deleteByKey() {
//        productInfoMapper.deleteById(15);
//        productInfoMapper.deleteByCode("1111");
//        productInfoMapper.deleteBySkuTypeAndColorType(1,11);
//    }
//
//}
