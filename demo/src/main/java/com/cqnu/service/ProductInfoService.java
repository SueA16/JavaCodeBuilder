package com.cqnu.service;

import com.cqnu.entity.po.ProductInfo;
import com.cqnu.entity.query.ProductInfoQuery;
import com.cqnu.entity.vo.PaginationResultVo;

import java.util.List;

/**
 * description:商品信息Service
 * author:小呆呆
 * date:2023/08/15
 */
public interface ProductInfoService {

	/**
	 * 根据条件查询列表
	 */
	List<ProductInfo> findListByParam(ProductInfoQuery query);

	/**
	 * 根据条件查询数量
	 */
	Integer findCountByParam(ProductInfoQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVo<ProductInfo> findListByPage(ProductInfoQuery query);

	/**
	 * 新增
	 */
	Integer add(ProductInfo bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<ProductInfo> listBean);

	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<ProductInfo> listBean);

	/**
	 * 根据 Id 查询
	 */
	ProductInfo getProductInfoById(Integer id);

	/**
	 * 根据 Id 更新
	 */
	Integer updateProductInfoById(ProductInfo bean, Integer id);

	/**
	 * 根据 Id 删除
	 */
	Integer deleteProductInfoById(Integer id);

	/**
	 * 根据 Code 查询
	 */
	ProductInfo getProductInfoByCode(String code);

	/**
	 * 根据 Code 更新
	 */
	Integer updateProductInfoByCode(ProductInfo bean, String code);

	/**
	 * 根据 Code 删除
	 */
	Integer deleteProductInfoByCode(String code);

	/**
	 * 根据 SkuTypeAndColorType 查询
	 */
	ProductInfo getProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType);

	/**
	 * 根据 SkuTypeAndColorType 更新
	 */
	Integer updateProductInfoBySkuTypeAndColorType(ProductInfo bean, Integer skuType, Integer colorType);

	/**
	 * 根据 SkuTypeAndColorType 删除
	 */
	Integer deleteProductInfoBySkuTypeAndColorType(Integer skuType, Integer colorType);

}