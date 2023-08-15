package com.cqnu.controller;

import com.cqnu.entity.po.ProductInfo;
import com.cqnu.entity.vo.ResponseVo;
import com.cqnu.entity.query.ProductInfoQuery;
import com.cqnu.service.ProductInfoService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * description:商品信息Controller
 * author:小呆呆
 * date:2023/08/15
 */
@RestController
@RequestMapping("/productInfo")
public class ProductInfoController extends BaseController {

	@Resource
	private ProductInfoService productInfoService;

	/**
	 * 根据条件查询列表
	 */
	@GetMapping("/getDataList")
	public ResponseVo getDataList(ProductInfoQuery query) {
		return getSuccessResponseVo(productInfoService.findListByPage(query));
	}

	/**
	 * 新增
	 */
	@PostMapping("/add")
	public Integer add(@RequestBody ProductInfo bean) {
		return this.productInfoService.add(bean);
	}

	/**
	 * 批量新增
	 */
	@PostMapping("/addBatch")
	public Integer addBatch(@RequestBody List<ProductInfo> listBean) {
		return this.productInfoService.addBatch(listBean);
	}

	/**
	 * 批量新增或修改
	 */
	@PostMapping("/addOrUpdateBatch")
	public Integer addOrUpdateBatch(@RequestBody List<ProductInfo> listBean) {
		return this.productInfoService.addOrUpdateBatch(listBean);
	}

	/**
	 * 根据 Id 查询
	 */
	@GetMapping("/getProductInfoById")
	public ProductInfo getProductInfoById(Integer id) {
		return this.productInfoService.getProductInfoById(id);
	}

	/**
	 * 根据 Id 更新
	 */
	@PutMapping("/updateProductInfoById")
	public Integer updateProductInfoById(@RequestBody ProductInfo bean, Integer id) {
		return this.productInfoService.updateProductInfoById(bean, id);
	}

	/**
	 * 根据 Id 删除
	 */
	@DeleteMapping("/deleteProductInfoById")
	public Integer deleteProductInfoById(Integer id) {
		return this.productInfoService.deleteProductInfoById(id);
	}

	/**
	 * 根据 Code 查询
	 */
	@GetMapping("/getProductInfoByCode")
	public ProductInfo getProductInfoByCode(String code) {
		return this.productInfoService.getProductInfoByCode(code);
	}

	/**
	 * 根据 Code 更新
	 */
	@PutMapping("/updateProductInfoByCode")
	public Integer updateProductInfoByCode(@RequestBody ProductInfo bean, String code) {
		return this.productInfoService.updateProductInfoByCode(bean, code);
	}

	/**
	 * 根据 Code 删除
	 */
	@DeleteMapping("/deleteProductInfoByCode")
	public Integer deleteProductInfoByCode(String code) {
		return this.productInfoService.deleteProductInfoByCode(code);
	}

	/**
	 * 根据 SkuTypeAndColorType 查询
	 */
	@GetMapping("/getProductInfoBySkuTypeAndColorType")
	public ProductInfo getProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType) {
		return this.productInfoService.getProductInfoBySkuTypeAndColorType(skuType, colorType);
	}

	/**
	 * 根据 SkuTypeAndColorType 更新
	 */
	@PutMapping("/updateProductInfoBySkuTypeAndColorType")
	public Integer updateProductInfoBySkuTypeAndColorType(@RequestBody ProductInfo bean, Integer skuType, Integer colorType) {
		return this.productInfoService.updateProductInfoBySkuTypeAndColorType(bean, skuType, colorType);
	}

	/**
	 * 根据 SkuTypeAndColorType 删除
	 */
	@DeleteMapping("/deleteProductInfoBySkuTypeAndColorType")
	public Integer deleteProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType) {
		return this.productInfoService.deleteProductInfoBySkuTypeAndColorType(skuType, colorType);
	}

}