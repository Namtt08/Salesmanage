package org.project.manage.response;

import java.util.List;

import org.project.manage.dto.ProductAllNameDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllProductNameListRespone extends MessageSuccessResponse{
	
	List <ProductAllNameDto> productName;

}
