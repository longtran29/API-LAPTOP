package com.springboot.laptop.service.impl;

import com.springboot.laptop.model.dto.response.CategoryRevenueDto;
import com.springboot.laptop.model.dto.response.DashboardDTO;
import com.springboot.laptop.model.dto.response.ReportDTO;
import com.springboot.laptop.repository.OrderRepository;
import com.springboot.laptop.service.AdminService;
import com.springboot.laptop.utils.JpaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Object dashboard() {

        ReportDTO report = new ReportDTO();

        DashboardDTO dto = new DashboardDTO();

        report.setPending(orderRepository.countNewOrders(0));
        report.setCancel(orderRepository.countRejectAndCancelToday());
        report.setToday(orderRepository.totalRevenueToday());
        report.setWeek(orderRepository.totalRevenueThisWeek());

        List<Object[]> objects = orderRepository.getCategoryRevenue();
        dto.setCategory(objects.stream().map(obj -> CategoryRevenueDto.builder()
                .id(JpaUtils.getString(obj[0]))
                .name(JpaUtils.getString(obj[1]))
                .revenue(JpaUtils.getLong(obj[2]))
                .build()).collect(Collectors.toList()));

        dto.setReport(report);
        return dto;
    }
}
