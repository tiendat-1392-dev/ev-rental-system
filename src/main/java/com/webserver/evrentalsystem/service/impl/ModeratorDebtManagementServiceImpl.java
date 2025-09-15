package com.webserver.evrentalsystem.service.impl;

import com.webserver.evrentalsystem.entity.*;
import com.webserver.evrentalsystem.entity.Debt;
import com.webserver.evrentalsystem.entity.Permission;
import com.webserver.evrentalsystem.entity.Role;
import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.exception.InvalidateParamsException;
import com.webserver.evrentalsystem.exception.PermissionDeniedException;
import com.webserver.evrentalsystem.model.DebtFilterByDate;
import com.webserver.evrentalsystem.model.DebtFilterByUser;
import com.webserver.evrentalsystem.model.dto.UpdateDebtRequest;
import com.webserver.evrentalsystem.model.dto.table.DebtInfoItem;
import com.webserver.evrentalsystem.model.dto.table.HeaderItem;
import com.webserver.evrentalsystem.model.dto.table.PagingResponse;
import com.webserver.evrentalsystem.model.mapping.DebtMapping;
import com.webserver.evrentalsystem.repository.DebtRepository;
import com.webserver.evrentalsystem.repository.ModeratorPermissionRepository;
import com.webserver.evrentalsystem.repository.UserRepository;
import com.webserver.evrentalsystem.service.ModeratorDebtManagementService;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ModeratorDebtManagementServiceImpl implements ModeratorDebtManagementService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private ModeratorPermissionRepository moderatorPermissionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void deleteDebtById(Long debtId, HttpServletRequest httpRequest) {

        Logger.printf("ModeratorDebtManagementServiceImpl.deleteDebtById");

        if (debtId == null) {
            throw new InvalidateParamsException("Truy vấn không hợp lệ!");
        }

        User manager = ManagerValidation.validateManager(userRepository, httpRequest);

        if (manager.getRole() != Role.ADMIN && !moderatorPermissionRepository.isHasPermission(manager.getUserName(), Permission.CHANGE_USER_PASSWORD.getKey())) {
            Logger.printf("Moderator " + manager.getUserName() + " không có quyền xoá khoản nợ");
            throw new PermissionDeniedException("Bạn không có quyền xoá khoản nợ này!");
        }

        Debt debt = debtRepository.findById(debtId).orElse(null);

        if (debt == null || debt.getIsDeleted()) {
            throw new InvalidateParamsException("Không thể tìm thấy khoản nợ!");
        }

        debt.setIsDeleted(true);
        debt.setDeletedBy(manager);
        debt.setDeleteAt(System.currentTimeMillis());
        debtRepository.save(debt);

        Logger.printf("Moderator " + manager.getUserName() + " đã xoá khoản nợ " + debt.getId());
    }

    @Override
    public void restoreDebtById(Long debtId, HttpServletRequest httpRequest) {
        if (debtId == null) {
            throw new InvalidateParamsException("Truy vấn không hợp lệ!");
        }

        User manager = ManagerValidation.validateManager(userRepository, httpRequest);

//        if (manager.getRole() != Role.ADMIN && !moderatorPermissionRepository.isHasPermission(manager.getUserName(), Permission.RESTORE_DEBT.getKey())) {
//            Logger.printf("Moderator " + manager.getUserName() + " không có quyền khôi phục khoản nợ");
//            throw new PermissionDeniedException("Bạn không có quyền khôi phục khoản nợ này!");
//        }

        Debt debt = debtRepository.findById(debtId).orElse(null);

        if (debt == null || debt.getIsDeleted()) {
            throw new InvalidateParamsException("Không thể tìm thấy khoản nợ!");
        }

        if (!debt.getIsPaid()) {
            throw new InvalidateParamsException("Không thể khôi phục khoản nợ chưa thanh toán!");
        }

        debt.setIsPaid(false);
        debt.setPaidDate(null);
        debt.setConfirmedPaymentBy(null);
        debtRepository.save(debt);

        Logger.printf("Moderator " + manager.getUserName() + " đã khôi phục khoản nợ " + debt.getId());
    }

    @Override
    public void updateDebt(UpdateDebtRequest updateDebtRequest, HttpServletRequest httpRequest) {
        User manager = ManagerValidation.validateManager(userRepository, httpRequest);

//        if (manager.getRole() != Role.ADMIN && !moderatorPermissionRepository.isHasPermission(manager.getUserName(), Permission.UPDATE_DEBT.getKey())) {
//            Logger.printf("Moderator " + manager.getUserName() + " không có quyền cập nhật khoản nợ");
//            throw new PermissionDeniedException("Bạn không có quyền cập nhật khoản nợ này!");
//        }

        if (!updateDebtRequest.isValid()) {
            throw new InvalidateParamsException("Truy vấn không hợp lệ!");
        }

        Debt debt = debtRepository.findById(updateDebtRequest.getDebtId()).orElse(null);

        if (debt == null || debt.getIsDeleted()) {
            throw new InvalidateParamsException("Không thể tìm thấy khoản nợ!");
        }

        if (debt.getIsPaid()) {
            throw new InvalidateParamsException("Khoản nợ đã được thanh toán!");
        }

        debt.setAmountOwed(updateDebtRequest.getOwedAmount());
        debt.setDescription(updateDebtRequest.getDescription());
        debt.setOwedDate(updateDebtRequest.getOwedDate());

        debtRepository.save(debt);

        Logger.printf("Moderator " + manager.getUserName() + " đã cập nhật khoản nợ " + debt.getId());
    }

    @Override
    public PagingResponse<DebtInfoItem> getDebts(String byDate, Long startDate, Long endDate, Boolean isPaid, String searchBy, String searchTerm, Integer pageNo, Integer pageSize, HttpServletRequest httpRequest) {

        ManagerValidation.validateManager(userRepository, httpRequest);

        // check validate params
        if (byDate == null || startDate == null || endDate == null || isPaid == null || searchBy == null || searchTerm == null || pageNo == null || pageSize == null) {
            throw new InvalidateParamsException("Truy vấn không hợp lệ!");
        }

        if (startDate > endDate) {
            throw new InvalidateParamsException("Truy vấn không hợp lệ!");
        }

        // nếu chưa trả mà byDate = "Ngày trả"
        if (!isPaid && byDate.equals(DebtFilterByDate.PAY_DATE.getValue())) {
            throw new InvalidateParamsException("Truy vấn không hợp lệ!");
        }

        if (!isPaid && searchBy.equals(DebtFilterByUser.CONFIRMED_BY_USER.getValue())) {
            throw new InvalidateParamsException("Truy vấn không hợp lệ!");
        }

        if (pageNo < 1 || pageSize < 1) {
            throw new InvalidateParamsException("Truy vấn không hợp lệ!");
        }

        List<Debt> debts = getDebtsByFilter(byDate, startDate, endDate, isPaid, searchBy, searchTerm, pageNo, pageSize);
        int totalPages = getTotalPages(byDate, startDate, endDate, isPaid, searchBy, searchTerm, pageSize);

        PagingResponse<DebtInfoItem> data = new PagingResponse<>();

        data.setCurrentPage(pageNo);
        data.setTotalPages(totalPages);
        data.setTotalElements(debts.size());

        List<HeaderItem> headerItemList = new ArrayList<>();
        headerItemList.add(new HeaderItem("Id", true));
        headerItemList.add(new HeaderItem("Tài khoản", false));
        headerItemList.add(new HeaderItem("Ngày nợ", false));
        headerItemList.add(new HeaderItem("Người nhập", false));
        headerItemList.add(new HeaderItem("Ngày nhập", false));
        headerItemList.add(new HeaderItem("Người thanh toán", !isPaid));
        headerItemList.add(new HeaderItem("Ngày thanh toán", !isPaid));
        headerItemList.add(new HeaderItem("Số tiền", false));
        headerItemList.add(new HeaderItem("Ghi chú", false));

        data.setHeader(headerItemList);

        List<DebtInfoItem> userDataItemList = new ArrayList<>();
        for (Debt debt : debts) {
            userDataItemList.add(DebtMapping.toDebtInfoItem(debt));
        }

        data.setRows(userDataItemList);
        data.setSearchTerm(searchTerm);

        return data;
    }

    private List<Debt> getDebtsByFilter(
            String byDate,
            Long startDate,
            Long endDate,
            Boolean isPaid,
            String searchBy,
            String searchTerm,
            Integer pageNo,
            Integer pageSize
    ) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Debt> query = builder.createQuery(Debt.class);
        Root<Debt> root = query.from(Debt.class);

        List<Predicate> predicates = getPredicateList(builder, root, byDate, startDate, endDate, isPaid, searchBy, searchTerm);

        query.select(root).where(predicates.toArray(new Predicate[0]));
        TypedQuery<Debt> typedQuery = entityManager.createQuery(query);

        // phân trang
        typedQuery.setFirstResult((pageNo - 1) * pageSize);
        typedQuery.setMaxResults(pageSize);

        return typedQuery.getResultList();
    }

    private int getTotalPages(
            String byDate,
            Long startDate,
            Long endDate,
            Boolean isPaid,
            String searchBy,
            String searchTerm,
            Integer pageSize
    ) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<Debt> root = countQuery.from(Debt.class);

        List<Predicate> predicates = getPredicateList(builder, root, byDate, startDate, endDate, isPaid, searchBy, searchTerm);

        countQuery.select(builder.count(root)).where(predicates.toArray(new Predicate[0]));

        int totalRows = Math.toIntExact(entityManager.createQuery(countQuery).getSingleResult());

        int totalPages = totalRows / pageSize;

        return totalRows % pageSize == 0 ? totalPages : totalPages + 1;
    }

    private List<Predicate> getPredicateList(
            CriteriaBuilder builder,
            Root<Debt> root,
            String byDate,
            Long startDate,
            Long endDate,
            Boolean isPaid,
            String searchBy,
            String searchTerm
    ) {
        List<Predicate> predicates = new ArrayList<>();

        if (byDate.equals(DebtFilterByDate.OWED_DATE.getValue())) {
            predicates.add(builder.between(root.get("owedDate"), startDate, endDate));
        } else if (byDate.equals(DebtFilterByDate.PAY_DATE.getValue())) {
            predicates.add(builder.between(root.get("paidDate"), startDate, endDate));
        } else if (byDate.equals(DebtFilterByDate.IMPORTED_DATE.getValue())) {
            predicates.add(builder.between(root.get("createdAt"), startDate, endDate));
        } else {
            throw new InvalidateParamsException("Truy vấn không hợp lệ!");
        }

        predicates.add(builder.equal(root.get("isPaid"), isPaid));

        if (!searchBy.isEmpty()) {
            if (searchBy.equals(DebtFilterByUser.DEBTOR.getValue())) {
                predicates.add(builder.like(root.get("debtor").get("userName"), "%" + searchTerm + "%" ));
            } else if (searchBy.equals(DebtFilterByUser.CREATOR.getValue())) {
                predicates.add(builder.like(root.get("creditor").get("userName"), "%" + searchTerm + "%" ));
            } else if (searchBy.equals(DebtFilterByUser.CONFIRMED_BY_USER.getValue())) {
                predicates.add(builder.like(root.get("confirmedPaymentBy").get("userName"), "%" + searchTerm + "%" ));
            } else {
                throw new InvalidateParamsException("Truy vấn không hợp lệ!");
            }
        }

        predicates.add(builder.equal(root.get("isDeleted"), false));

        return predicates;
    }
}
