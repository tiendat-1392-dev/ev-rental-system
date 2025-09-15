package com.webserver.evrentalsystem.service.impl;

import com.webserver.evrentalsystem.entity.*;
import com.webserver.evrentalsystem.entity.Permission;
import com.webserver.evrentalsystem.entity.Role;
import com.webserver.evrentalsystem.entity.TopupRequest;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.PermissionDeniedException;
import com.webserver.evrentalsystem.model.TopupRequestHistoryFilterByStatus;
import com.webserver.evrentalsystem.model.TopupRequestHistoryFilterByUser;
import com.webserver.evrentalsystem.model.dto.RejectTopupRequest;
import com.webserver.evrentalsystem.model.dto.table.*;
import com.webserver.evrentalsystem.model.dto.table.HeaderItem;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import com.webserver.evrentalsystem.model.dto.table.TopupHistoryItem;
import com.webserver.evrentalsystem.model.dto.table.TopupRequestPagingResponse;
import com.webserver.evrentalsystem.model.mapping.TopupRequestMapping;
import com.webserver.evrentalsystem.repository.ModeratorPermissionRepository;
import com.webserver.evrentalsystem.repository.TopupRequestRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.service.ModeratorTopupRequestService;
import com.webserver.evrentalsystem.service.validation.ManagerValidation;
import com.webserver.evrentalsystem.utils.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ModeratorTopupRequestServiceImpl implements ModeratorTopupRequestService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopupRequestRepository topupRequestRepository;

    @Autowired
    private ModeratorPermissionRepository moderatorPermissionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public TopupRequestPagingResponse getTopupRequests(Integer pageNo, Integer pageSize, String searchTerm, HttpServletRequest httpRequest) {

        if (pageNo < 1 || pageSize < 1 || searchTerm == null) {
            throw new InvalidateParamsException("Truy vấn không hợp lệ!");
        }

        ManagerValidation.validateManager(userRepository, httpRequest);

        Page<TopupRequest> page = topupRequestRepository.findAllWaitingRequestIn(searchTerm, PageRequest.of(Math.max((pageNo - 1), 0), pageSize));

        TopupRequestPagingResponse data = new TopupRequestPagingResponse();

        data.setTotalPages(page.getTotalPages());
        data.setTotalElements(page.getTotalElements());
        data.setCurrentPage(pageNo);
        data.setData(TopupRequestMapping.toTopupRequestGroups(page.getContent()));

        return data;
    }

    @Override
    public void approveTopupRequests(List<Long> topupRequestIds, HttpServletRequest httpRequest) {

        if (topupRequestIds == null || topupRequestIds.isEmpty()) {
            throw new InvalidateParamsException("Yêu cầu nạp tiền không tồn tại!");
        }

        User manager = ManagerValidation.validateManager(userRepository, httpRequest);

        if (manager.getRole() != Role.ADMIN && !moderatorPermissionRepository.isHasPermission(manager.getUserName(), Permission.APPROVE_TOPUP_REQUEST.getKey())) {
            Logger.printf("Moderator " + manager.getUserName() + " không có quyền duyệt yêu cầu nạp tiền!");
            throw new PermissionDeniedException("Bạn không có quyền duyệt yêu cầu nạp tiền!");
        }

        for (Long topupRequestId : topupRequestIds) {

            // tìm yêu cầu, kiểm tra tính xác thực
            // nếu yêu cầu không tồn tại hoặc đã bị xóa thì bỏ qua
            // nếu yêu cầu đã được duyệt hoặc bị từ chối thì bỏ qua

            TopupRequest topupRequest = topupRequestRepository.findById(topupRequestId).orElse(null);

            if (topupRequest == null || topupRequest.getIsDeleted()) {
                throw new InvalidateParamsException("Yêu cầu nạp tiền không tồn tại!");
            }

            if (topupRequest.getIsApproved()) {
                throw new InvalidateParamsException("Yêu cầu này đã được duyệt trước đó!");
            }

            if (topupRequest.getIsRejected()) {
                throw new InvalidateParamsException("Yêu cầu này đã bị từ chối trước đó!");
            }

            topupRequest.setIsApproved(true);
            topupRequest.setApprovedBy(manager);
            topupRequest.setApprovedAt(System.currentTimeMillis());

            topupRequestRepository.save(topupRequest);

            // TODO: thông báo cho người dùng biết yêu cầu của họ đã được duyệt
        }
    }

    @Override
    public void rejectTopupRequest(RejectTopupRequest rejectTopupRequest, HttpServletRequest httpRequest) {

        if (!rejectTopupRequest.isValid()) {
            throw new InvalidateParamsException("Truy vấn không hợp lệ!");
        }

        User manager = ManagerValidation.validateManager(userRepository, httpRequest);

        if (manager.getRole() != Role.ADMIN && !moderatorPermissionRepository.isHasPermission(manager.getUserName(), Permission.REJECT_TOPUP_REQUEST.getKey())) {
            Logger.printf("Moderator " + manager.getUserName() + " không có quyền từ chối yêu cầu nạp tiền!");
            throw new PermissionDeniedException("Bạn không có quyền từ chối yêu cầu nạp tiền!");
        }

        for (Long topupRequestId : rejectTopupRequest.getTopupRequestIds()) {

            TopupRequest topupRequest = topupRequestRepository.findById(topupRequestId).orElse(null);

            if (topupRequest == null || topupRequest.getIsDeleted()) {
                throw new InvalidateParamsException("Yêu cầu nạp tiền không tồn tại!");
            }

            if (topupRequest.getIsApproved()) {
                throw new InvalidateParamsException("Yêu cầu này đã được duyệt trước đó!");
            }

            if (topupRequest.getIsRejected()) {
                throw new InvalidateParamsException("Yêu cầu này đã bị từ chối trước đó!");
            }

            topupRequest.setIsRejected(true);
            topupRequest.setRejectedBy(manager);
            topupRequest.setRejectedAt(System.currentTimeMillis());
            topupRequest.setReason(rejectTopupRequest.getReason());
            topupRequestRepository.save(topupRequest);

            // hoàn trả tiền cho người dùng
            User user = topupRequest.getUser();
            user.setAmount(user.getAmount() + topupRequest.getAmount());
            userRepository.save(user);

            // TODO: thông báo cho người dùng biết yêu cầu của họ đã bị từ chối
        }
    }

    @Override
    public PagingResponse<TopupHistoryItem> getTopupRequestHistory(Long startDate, Long endDate, String status, String searchBy, String searchTerm, Integer pageNo, Integer pageSize, HttpServletRequest httpRequest) {

        ManagerValidation.validateManager(userRepository, httpRequest);

        // check validate params
        if (searchBy == null || startDate == null || endDate == null || status == null || searchTerm == null || pageNo == null || pageSize == null) {
            throw new InvalidateParamsException("Truy vấn không hợp lệ!");
        }

        if (startDate > endDate) {
            throw new InvalidateParamsException("Truy vấn không hợp lệ!");
        }

        if (pageNo < 1 || pageSize < 1) {
            throw new InvalidateParamsException("Truy vấn không hợp lệ!");
        }

        List<TopupRequest> topupRequests = getTopupRequestsByFilter(startDate, endDate, status, searchBy, searchTerm, pageNo, pageSize);
        int totalPages = getTotalPages(startDate, endDate, status, searchBy, searchTerm, pageSize);

        PagingResponse<TopupHistoryItem> data = new PagingResponse<>();

        data.setCurrentPage(pageNo);
        data.setTotalPages(totalPages);
        data.setTotalElements(topupRequests.size());

        boolean filterByApproved = status.equals(TopupRequestHistoryFilterByStatus.APPROVED.getValue());
        boolean filterByRejected = status.equals(TopupRequestHistoryFilterByStatus.REJECTED.getValue());

        List<HeaderItem> headerItemList = new ArrayList<>();
        headerItemList.add(new HeaderItem("Id", true));
        headerItemList.add(new HeaderItem("Tài khoản", false));
        headerItemList.add(new HeaderItem("Số tiền", false));
        headerItemList.add(new HeaderItem("Chấp nhận bởi", !filterByApproved));
        headerItemList.add(new HeaderItem("Ngày chấp nhận", !filterByApproved));
        headerItemList.add(new HeaderItem("Từ chối bởi", !filterByRejected));
        headerItemList.add(new HeaderItem("Ngày từ chối", !filterByRejected));
        headerItemList.add(new HeaderItem("Lý do", !filterByRejected));
        headerItemList.add(new HeaderItem("Ngày tạo", false));

        data.setHeader(headerItemList);

        List<TopupHistoryItem> itemList = new ArrayList<>();
        for (TopupRequest topupRequest : topupRequests) {
            itemList.add(TopupRequestMapping.toTopupHistoryItem(topupRequest));
        }

        data.setRows(itemList);
        data.setSearchTerm(searchTerm);

        return data;
    }

    private List<TopupRequest> getTopupRequestsByFilter(
            Long startDate,
            Long endDate,
            String status,
            String searchBy,
            String searchTerm,
            Integer pageNo,
            Integer pageSize
    ) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TopupRequest> query = builder.createQuery(TopupRequest.class);
        Root<TopupRequest> root = query.from(TopupRequest.class);

        List<Predicate> predicates = getPredicateList(builder, root, startDate, endDate, status, searchBy, searchTerm);

        query.select(root).where(predicates.toArray(new Predicate[0]));
        TypedQuery<TopupRequest> typedQuery = entityManager.createQuery(query);

        // phân trang
        typedQuery.setFirstResult((pageNo - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);

        return typedQuery.getResultList();
    }

    private int getTotalPages(
            Long startDate,
            Long endDate,
            String status,
            String searchBy,
            String searchTerm,
            Integer pageSize
    ) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<TopupRequest> root = countQuery.from(TopupRequest.class);

        List<Predicate> predicates = getPredicateList(builder, root, startDate, endDate, status, searchBy, searchTerm);

        countQuery.select(builder.count(root)).where(predicates.toArray(new Predicate[0]));

        int totalRows = Math.toIntExact(entityManager.createQuery(countQuery).getSingleResult());

        int totalPages = totalRows / pageSize;

        return totalRows % pageSize == 0 ? totalPages : totalPages + 1;
    }

    private List<Predicate> getPredicateList(
            CriteriaBuilder builder,
            Root<TopupRequest> root,
            Long startDate,
            Long endDate,
            String status,
            String searchBy,
            String searchTerm
    ) {
        List<Predicate> predicates = new ArrayList<>();

        if (status.equals(TopupRequestHistoryFilterByStatus.APPROVED.getValue())) {
            predicates.add(builder.equal(root.get("isApproved"), true));
            predicates.add(builder.between(root.get("approvedAt"), startDate, endDate));
        } else if (status.equals(TopupRequestHistoryFilterByStatus.REJECTED.getValue())) {
            predicates.add(builder.equal(root.get("isRejected"), true));
            predicates.add(builder.between(root.get("rejectedAt"), startDate, endDate));
        } else {
            throw new InvalidateParamsException("Truy vấn không hợp lệ!");
        }

        if (!searchBy.isEmpty()) {
            if (searchBy.equals(TopupRequestHistoryFilterByUser.USER.getValue())) {
                predicates.add(builder.like(root.get("user").get("userName"), "%" + searchTerm + "%" ));
            } else if (searchBy.equals(TopupRequestHistoryFilterByUser.STAFF.getValue())) {
                if (status.equals(TopupRequestHistoryFilterByStatus.APPROVED.getValue())) {
                    predicates.add(builder.like(root.get("approvedBy").get("userName"), "%" + searchTerm + "%" ));
                } else if (status.equals(TopupRequestHistoryFilterByStatus.APPROVED.getValue())) {
                    predicates.add(builder.like(root.get("rejectedBy").get("userName"), "%" + searchTerm + "%" ));
                } else {
                    throw new InvalidateParamsException("Truy vấn không hợp lệ!");
                }
            } else {
                throw new InvalidateParamsException("Truy vấn không hợp lệ!");
            }
        }

        predicates.add(builder.equal(root.get("isDeleted"), false));

        return predicates;
    }
}
