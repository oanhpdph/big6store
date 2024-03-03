package com.poly.service.Impl;

import com.poly.common.CheckLogin;
import com.poly.common.RandomNumber;
import com.poly.dto.*;
import com.poly.entity.*;
import com.poly.repository.*;
import com.poly.service.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BillImpl implements BillService {

    java.util.Date today = new java.util.Date();

    Date threedayago = new Date(today.getTime() - (1000 * 60 * 60 * 24 * 3));

    @Autowired
    private BillRepos billRepos;

    @Autowired
    private BillProductRepos billProductRepos;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    BillStatusRepos billStatusRepos;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private VoucherCustomerService voucherCustomerService;

    @Autowired
    ProductDetailRepo productDetailRepo;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PaymentMethodRepos paymentMethodRepos;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private ImageReturnService imageReturnService;

    @Autowired
    BillProductService billProductService;

    @Autowired
    BillStatusService billStatusService;

    @Autowired
    DeliveryNotesRepos deliveryNotesRepos;

    @Autowired
    private CheckLogin checkLogin;

    @Autowired
    HistoryBillProductRepository historyBillProductRepository;


    @Autowired
    HistoryBillProductService historyBillProductService;


    @Override
    public List<Bill> all() {
        return billRepos.findAll();
    }


    @Override
    public Bill add(BillProRes bill) {
        String code = "";
        do {
            code = RandomNumber.generateRandomString(10);
        } while (billRepos.findByCode(code) == null);
        Bill bi = new Bill();
        Optional<Voucher> voucher = voucherService.findById(bill.getVoucher());
        if (voucher.isPresent()) {
            UserDetailDto userDetailDto = checkLogin.checkLogin();
            if (userDetailDto != null) {
                List<VoucherCustomer> voucherCustomer = voucherCustomerService.findByUser(userDetailDto.getId()).stream().filter(voucherCustomer1 -> voucherCustomer1.getVoucher().getId() == voucher.get().getId()).toList();
                VoucherCustomerRes voucherCustomerRes = new VoucherCustomerRes();
                voucherCustomerRes.setCustomer(userDetailDto.getId());
                voucherCustomerRes.setVoucher(voucher.get().getId());
                voucherCustomerRes.setActive(false);
                voucherCustomerService.updateById(voucherCustomerRes, voucherCustomer.get(0).getId());

                bi.setVoucher(voucher.get());
                bi.setVoucherValue(BigDecimal.valueOf(voucher.get().getValue()));
                bill.setTotalPrice(bill.getTotalPrice().subtract(BigDecimal.valueOf(voucher.get().getValue())));
            }
        } else {
            bi.setVoucherValue(BigDecimal.valueOf(0));
        }

        bi.setCustomer(bill.getCustomer());
        bi.setCode("HD" + code);
        bi.setCreateDate(new java.util.Date());
        bi.setPaymentDate(new java.util.Date());
        bi.setTotalPrice(bill.getTotalPrice());
        bi.setPaymentStatus(2);
        bi.setBillStatus(billStatusRepos.findByCode("WP").get());
        bi.setPaymentMethod(paymentMethodRepos.findById(1).get());

        return this.billRepos.save(bi);
    }

    @Override
    public void addBillPro(Bill bill, BillProRes billProRes) {
        List<Optional<ProductDetail>> list = new ArrayList<>();
        for (Integer id : billProRes.getProduct()) {
            list.add(productDetailRepo.findById(id));
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isPresent()) {
                BillProduct billProduct = new BillProduct();
                billProduct.setBill(bill);
                billProduct.setProduct(list.get(i).get());
                billProduct.setPrice(list.get(i).get().getPriceExport());
                billProduct.setQuantity(billProRes.getQuantity().get(i));
                billProduct.setStatus(0);
                billProduct.setReducedMoney(billProRes.getReducedMoney().get(i));
                this.billProductRepos.save(billProduct);
            }
        }
    }

    @Override
    public Page<Bill> loadData(SearchBillDto searchBillDto, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Bill> billCriteriaQuery = criteriaBuilder.createQuery(Bill.class);
        Root<Bill> billRoot = billCriteriaQuery.from(Bill.class);

        List<Predicate> list = new ArrayList<Predicate>();
        List<String> billStatus = new ArrayList<>();
        if (searchBillDto.getBillStatus().equals("doncho") && searchBillDto.getBillStatus().equals("")) {
            billStatus.add("WP");
        } else if (searchBillDto.getBillStatus().equals("chuanbi")) {
            billStatus.add("PG");
        } else if (searchBillDto.getBillStatus().equals("danggiao")) {
            billStatus.add("DE");
        } else if (searchBillDto.getBillStatus().equals("hoanthanh")) {
            billStatus.add("CO");
        } else if (searchBillDto.getBillStatus().equals("donhuy")) {
            list.add(criteriaBuilder.equal(billRoot.get("billStatus").get("code"), "CA"));
        } else if (searchBillDto.getBillStatus().equals("dontra")) {
            list.add(criteriaBuilder.or(criteriaBuilder.equal(billRoot.get("billStatus").get("code"), "RR"),
                    criteriaBuilder.equal(billRoot.get("billStatus").get("code"), "WR"),
                    criteriaBuilder.equal(billRoot.get("billStatus").get("code"), "RE")
            ));
        } else if (searchBillDto.getBillStatus().equals("donhoan")) {
            billStatus.add("RN");
        } else {
            billStatus.add("WP");
        }

        if (!searchBillDto.getKey().isEmpty()) {
            list.add(criteriaBuilder.or(criteriaBuilder.equal(billRoot.get("code"), searchBillDto.getKey()),
                    criteriaBuilder.like(billRoot.get("customer").get("name"), "%" + searchBillDto.getKey() + "%"),
                    criteriaBuilder.like(billRoot.get("customer").get("phoneNumber"), "%" + searchBillDto.getKey() + "%")));
        }
        if (searchBillDto.getPaymentStatus() != -1) {
            list.add(criteriaBuilder.equal(billRoot.get("paymentStatus"), searchBillDto.getPaymentStatus()));
        }
        for (String s : billStatus) {
            list.add(criteriaBuilder.or(criteriaBuilder.equal(billRoot.get("billStatus").get("code"), s)));
        }

        if (!searchBillDto.getDate().isEmpty()) {
            String date1 = searchBillDto.getDate().substring(0, searchBillDto.getDate().indexOf("-") - 1).replace("/", "-");
            String date2 = searchBillDto.getDate().substring(searchBillDto.getDate().indexOf("-") + 1, searchBillDto.getDate().length()).replace("/", "-");
            Date dateStart = Date.valueOf(date1.trim());
            Date dateEnd = Date.valueOf(date2.trim());
            list.add(criteriaBuilder.between(billRoot.get("createDate"), dateStart, dateEnd));
        }
        if (searchBillDto.getSort() == 1) {
            billCriteriaQuery.orderBy(criteriaBuilder.desc(billRoot.get("createDate")));
        }
        billCriteriaQuery.where(criteriaBuilder.and(list.toArray(new Predicate[list.size()])));
        List<Bill> result = entityManager.createQuery(billCriteriaQuery).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        List<Bill> result2 = entityManager.createQuery(billCriteriaQuery).getResultList();
        Page<Bill> page = new PageImpl<>(result, pageable, result2.size());
        return page;
    }

    @Override
    public Bill add(Bill bill) {
        return billRepos.save(bill);
    }

    @Override
    public Bill update(Bill bill, Integer id) {
        Optional<Bill> optional = billRepos.findById(id);
        if (optional.isPresent()) {
            Bill billUpdate = optional.get();
            if (bill.getBillStatus() != null) {
                if ("CO".equals(bill.getBillStatus().getCode())) {
                    DeliveryNotes deliveryNotes = billUpdate.getDeliveryNotes().get(0);
                    deliveryNotes.setReceivedDate(new java.util.Date());
                    deliveryNotesRepos.save(deliveryNotes);
                }
                billUpdate.setBillStatus(bill.getBillStatus());
            }
            if (bill.getPaymentMethod() != null) {
                billUpdate.setPaymentMethod(bill.getPaymentMethod());
            }
            if (bill.getPaymentStatus() != -1) {
                billUpdate.setPaymentStatus(bill.getPaymentStatus());
            }
            return billRepos.save(billUpdate);
        }
        return null;
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<Bill> optional = billRepos.findById(id);
        if (optional.isPresent()) {
            billRepos.delete(optional.get());
            return true;
        }
        return false;
    }

    @Override
    public Bill getOneById(Integer id) {
        Optional<Bill> optional = billRepos.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }


    @Override
    public List<Bill> findAllBillByUser(Integer id) {
        List<Bill> dto = this.billRepos.findBillByUser(id);
        return dto;
    }

    @Override
    public Optional<Bill> findByCode(String code) {
        return this.billRepos.findByCode(code);
    }


    @Override
    public void logicBillReturn(Integer id, List<ReturnDto> returnDto) {
        Bill bill = this.billRepos.findById(id).get();
        Optional<Integer> returnCount = this.historyBillProductRepository.findReturnCountBillById(id);
        Integer count = 0;
        if (returnCount.isPresent()) {
            count = returnCount.get() + 1;
        } else {
            count = 1;
        }
        for (ReturnDto dto : returnDto) {
            BillProduct billProduct = this.billProductService.edit(dto.getIdBillProduct());
            billProduct.setStatus(1);
            this.billProductService.save(billProduct);
            HistoryBillProduct historyBillProduct = new HistoryBillProduct();
            historyBillProduct.setBillProduct(billProduct);
            historyBillProduct.setQuantityRequestReturn(Integer.parseInt(dto.getQuantityReturn()));
            historyBillProduct.setQuantityAcceptReturn(0);
            historyBillProduct.setReason(dto.getReason());
            historyBillProduct.setDate(today);
            historyBillProduct.setStatusBillProduct(1);
            historyBillProduct.setBank(dto.getBank());
            historyBillProduct.setOwner(dto.getOwner());
            historyBillProduct.setReturnMethod(1);
            historyBillProduct.setAccountNumber(dto.getAccountNumber());
            historyBillProduct.setStatus(1);
            historyBillProduct.setBill(bill);
            historyBillProduct.setReturnTimes(count);
            this.historyBillProductService.save(historyBillProduct);
            for (ImageReturnDto image : dto.getImage()) {
                ImageReturned img = new ImageReturned();
                img.setHBillProduct(historyBillProduct);
                img.setNameImage(image.getNameImage());
                this.imageReturnService.save(img);
            }
        }
    }

    @Override
    public List<Boolean> checkValidationReturn(int idStatus) {
        UserDetailDto customerUserDetail = this.checkLogin.checkLogin();
        List<Bill> billList = this.findAllBillByUser(customerUserDetail.getId());
        if(idStatus == 4){
            billList= billList.stream().filter(bill -> bill.getBillStatus().getId()==4).toList();
        }
        List<Boolean> listeck = new ArrayList<>();
        for (int i = 0; i < billList.size(); i++) {
            if (billList.get(i).getDeliveryNotes().get(0).getReceivedDate() == null) {
                boolean check = false;
                listeck.add(check);
                continue;
            }
            if (billList.get(i).getDeliveryNotes().get(0).getReceivedDate().compareTo(threedayago) >= 0) {
                boolean check = true;
                listeck.add(check);
            } else {
                boolean check = false;
                listeck.add(check);
            }
        }
        return listeck;
    }


    @Override
    public Boolean checkValidateReturnNologin(String search) {
        Optional<Bill> bill = this.findByCode(search.trim());
        boolean check = false;
        if (bill.get().getDeliveryNotes().get(0).getReceivedDate() != null) {
            if (bill.get().getDeliveryNotes().get(0).getReceivedDate().compareTo(threedayago) >= 0) {
                check = true;
            } else {
                check = false;
            }
        }
        return check;
    }

    @Override
    public List<Bill> findBillReturnByStatus(String code) {
        return this.billRepos.findBillReturn(code);
    }

    @Override
    public Bill findBillNewReturnByCode(String code) {
        Optional<Bill> bill = this.billRepos.findByCode(code);
        if (!bill.isPresent()) {
            return null;
        }
//        List<CountBillProductReturnDto> billProductDTOList = new ArrayList<>();
//        List<Object[]> resultList = this.historyBillProductRepository.getReturnedDataForBill(Long.parseLong(String.valueOf(bill.get().getId())));
//        for (Object[] result : resultList) {
//            CountBillProductReturnDto count = new CountBillProductReturnDto();
//            count.setIdBillProduct((Integer) result[0]);
//            count.setTotalAcceptReturn(Integer.parseInt(String.valueOf((long) result[1])));
//            billProductDTOList.add(count);
//        }
//        for (CountBillProductReturnDto countBilProductReturnDto : billProductDTOList) {
//            for (BillProduct billProduct : bill.get().getBillProducts()) {
//                if (countBilProductReturnDto.getIdBillProduct() == billProduct.getId()) {
//                    billProduct.setQuantity(billProduct.getQuantity() - countBilProductReturnDto.getTotalAcceptReturn());
//                }
//            }
//        }
        return bill.get();
    }


    @Override
    public Boolean checkConditionReturnNoLogin(String code) {
        Bill bill = this.billRepos.findByCode(code).get();
        boolean check = false;
        for (BillProduct billProduct : bill.getBillProducts()) {
            if (billProduct.getQuantity() != 0) {
                check = true;
            }
        }
        return check;
    }

    @Override
    public Boolean checkQuantityBillReturn(Integer id) {
        Bill bill = this.billRepos.findById(id).get();
        boolean check = false;
        for (BillProduct billProduct : bill.getBillProducts()) {
            if (billProduct.getQuantity() != 0) {
                check = true;
            }
        }
        return check;
    }

    @Override
    public List<Bill> findBillByUserAndStatus(Integer idUser, Integer status) {
        return this.billRepos.findBillByUserAndStatus(idUser, status);
    }


}
