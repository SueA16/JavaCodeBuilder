//package test;
//
//import com.cqnu.DemoApplication;
//import com.cqnu.entity.po.ProductInfo;
//import com.cqnu.entity.query.ProductInfoQuery;
//import com.cqnu.mapper.ProductInfoMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//import java.util.List;
//
//@SpringBootTest(classes = DemoApplication.class)
//class MapperTest {
//
//    @Resource
//    private ProductInfoMapper<ProductInfo, ProductInfoQuery> productInfoMapper;
//
//    @Test
//    void selectList() {
//        List<ProductInfo> productInfos = productInfoMapper.selectList(new ProductInfoQuery());
//        System.out.println(productInfos.size());
//    }
//
//}
