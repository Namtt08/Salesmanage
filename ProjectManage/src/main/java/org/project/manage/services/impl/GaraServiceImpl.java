package org.project.manage.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jdk.nashorn.internal.runtime.options.Option;
import org.apache.commons.lang3.StringUtils;
import org.project.manage.dto.GaraInfoDto;
import org.project.manage.entities.GaraInfoEntity;
import org.project.manage.entities.User;
import org.project.manage.repository.GaraRepository;
import org.project.manage.response.GaraDetailResponse;
import org.project.manage.response.GaraListResponse;
import org.project.manage.services.GaraService;
import org.project.manage.util.AppResultCode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GaraServiceImpl implements GaraService {

    @Autowired
    private GaraRepository garaRepository;

    @Override
    public GaraListResponse getAllGaraInfo() {
        GaraListResponse response = new GaraListResponse();
        List<GaraInfoDto> listGaraInfo = new ArrayList<>();
        try {
            List<GaraInfoEntity> listGara = garaRepository.findAll();
            if (!Objects.isNull(listGara)) {
                for (GaraInfoEntity entity : listGara) {
                    if (StringUtils.isBlank(entity.getDeleteBy()) && entity.isStatus()) {
                        GaraInfoDto dto = new GaraInfoDto();
                        dto.setId(entity.getId());
                        dto.setGaraCode(entity.getGaraCode());
                        dto.setGaraName(entity.getGaraName());
                        dto.setPhone(entity.getPhone());
                        dto.setLatitude(entity.getLatitude());
                        dto.setLongitude(entity.getLongitude());
                        dto.setGaraAddress(entity.getGaraAddress());
                        dto.setStatus(entity.isStatus());
                        if (entity.getDiscount() == null) {
                            dto.setDiscountRate((double) 0);
                        } else {
                            dto.setDiscountRate((double) entity.getDiscount());
                        }

                        String url = garaRepository.getGaraAvatar(entity.getId());
                        dto.setDocPath(url);
                        if (!StringUtils.isBlank(url) && entity.getLatitude() != null || entity.getLongitude() != null) {
                            listGaraInfo.add(dto);
                        }
                    }
                }
                response.setListGara(listGaraInfo);
            }
            return response;
        } catch (Exception e) {
            log.error("#getAllProductCategory#ERROR#:" + e.getMessage());
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public GaraDetailResponse getGaraDetail(Long id, User user) {

        GaraDetailResponse response = new GaraDetailResponse();

        GaraInfoDto garaInfoDto = new GaraInfoDto();
        Optional<GaraInfoEntity> garaInfoEntityOptional = garaRepository.findByIdAndDeleteByIsNull(id);
        if (garaInfoEntityOptional.isPresent()) {
            GaraInfoEntity entity = garaInfoEntityOptional.get();
            BeanUtils.copyProperties(garaInfoEntityOptional.get(), garaInfoDto, GaraInfoDto.class);
            if (entity.getDiscount() == null) {
                garaInfoDto.setDiscountRate((double) 0);
            } else {
                garaInfoDto.setDiscountRate((double) entity.getDiscount());
            }

            response.setGaraInfo(garaInfoDto);
        } else {
            response.setCodeStatus(AppResultCode.ERROR);
            response.setMessageStatus("No Data");
        }
        return response;
    }


}
