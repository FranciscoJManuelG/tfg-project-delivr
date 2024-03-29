import React from 'react';
import {useSelector} from 'react-redux';
import {Route, Switch} from 'react-router-dom';

import AppGlobalComponents from './AppGlobalComponents';
import {Login, SignUp, UpdateProfile, ChangePassword, Logout, SignUpBusinessman, AddFavouriteAddress, FindFavouriteAddresses, FindFavouriteAddressesResult, PayFee, CheckPayFee} from '../../users';
import {AddCompany, ModifyCompany, FindCompanyAddresses, FindCompanyAddressesResult, AddCompanyAddress, StateCompany, AddGoal, FindGoals, FindGoalsResult, EditGoal, FindGoalToEdit, FindCompanyCategoriesResult, FindCitiesResult} from '../../business';
import {FindCompaniesByAddress, FindCompaniesResult, FindAllCompaniesResult, FindAllCompaniesCriteria} from '../../businessCatalog';
import {FindProductsByCompanyToDeliverResult, FindProductsByCompanyForReservationsResult} from '../../productCatalog';
import {FindProductsResult, FindProducts, AddProduct, EditProduct, FindProductToEdit, FindProductCategoriesResult} from '../../productManagement';
import {FindShoppingCartProducts, PurchaseDetails, SetAddressToSendPurchase, ShowFavAddresses, PurchaseCompleted, OrderDetails, FindUserOrdersResult, FindCompanyOrdersResult, FindCompanyOrders, FindUserOrders, FindDiscountTicketsResult, FindDiscountTickets} from '../../shopping';
import users from '../../users';
import {FindMenuProducts, ReservationDetails, ReservationCompleted, SetDateAndDiners, ReserveDetails, FindUserReserves, FindUserReservesResult, FindCompanyReservesResult, SetCriteriaForCompanyReserves, FindCompanyReserves, EvaluationCompleted, CalculateDeposit} from '../../reservation';
import AddEventEvaluation from '../../reservation/components/AddEventEvaluation';
import FindUserEventEvaluations from '../../reservation/components/FindUserEventEvaluations';
import FindUserEventEvaluationsResult from '../../reservation/components/FindUserEventEvaluationsResult';

const Body = () => {

    const loggedIn = useSelector(users.selectors.isLoggedIn);
    
   return (

        <div className="container" >
            <br/>
            <AppGlobalComponents/>
            <Switch>
                <Route exact path="/"><FindCompaniesByAddress/></Route>
                {<Route exact path="/businessCatalog/find-companies-by-address"><FindCompaniesByAddress/></Route>}
                {<Route exact path="/businessCatalog/find-companies-result"><FindCompaniesResult/></Route>}
                {<Route exact path="/productCatalog/find-products-by-company-to-deliver-result/:id/:doReserve/:cityId"><FindProductsByCompanyToDeliverResult/></Route>}
                {<Route exact path="/productCatalog/find-products-by-company-for-reservations-result/:id/:reservationDate/:periodType/:diners"><FindProductsByCompanyForReservationsResult/></Route>}
                {loggedIn && <Route exact path="/management/find-products"><FindProducts/></Route>}
                {loggedIn && <Route exact path="/management/add-product"><AddProduct/></Route>}
                {loggedIn && <Route exact path="/management/find-product-to-edit/:id"><FindProductToEdit/></Route>}
                {loggedIn && <Route exact path="/management/edit-product"><EditProduct/></Route>}
                {loggedIn && <Route exact path="/management/find-products-result"><FindProductsResult/></Route>}
                {loggedIn && <Route exact path="/users/update-profile"><UpdateProfile/></Route>}
                {loggedIn && <Route exact path="/users/change-password"><ChangePassword/></Route>}
                {loggedIn && <Route exact path="/users/logout"><Logout/></Route>}
                {loggedIn && <Route exact path="/users/add-favourite-address"><AddFavouriteAddress/></Route>}
                {loggedIn && <Route exact path="/users/find-favourite-addresses"><FindFavouriteAddresses/></Route>}
                {loggedIn && <Route exact path="/users/find-favourite-addresses-result"><FindFavouriteAddressesResult/></Route>}
                {loggedIn && <Route exact path="/business/add-company"><AddCompany/></Route>}
                {loggedIn && <Route exact path="/business/modify-company"><ModifyCompany/></Route>}
                {loggedIn && <Route exact path="/business/find-company-addresses"><FindCompanyAddresses/></Route>}
                {loggedIn && <Route exact path="/business/find-company-addresses-result"><FindCompanyAddressesResult/></Route>}
                {loggedIn && <Route exact path="/business/add-company-address"><AddCompanyAddress/></Route>}
                {loggedIn && <Route exact path="/business/state-company"><StateCompany/></Route>}
                {loggedIn && <Route exact path="/business/add-goal"><AddGoal/></Route>}
                {loggedIn && <Route exact path="/business/find-goals"><FindGoals/></Route>}
                {loggedIn && <Route exact path="/business/find-goals-result"><FindGoalsResult/></Route>}
                {loggedIn && <Route exact path="/business/edit-goal"><EditGoal/></Route>}
                {loggedIn && <Route exact path="/business/find-goal-to-edit/:id"><FindGoalToEdit/></Route>}
                {<Route exact path="/shopping/find-shopping-cart-products/:id/:doReserve/:cityId"><FindShoppingCartProducts/></Route>}
                {loggedIn && <Route exact path="/shopping/purchase-details/:id"><PurchaseDetails/></Route>}
                {loggedIn && <Route exact path="/shopping/set-address-to-send-purchase/:id/:companyCityId"><SetAddressToSendPurchase/></Route>}
                {loggedIn && <Route exact path="/shopping/show-fav-addresses/:id/:cityId"><ShowFavAddresses/></Route>}
                {loggedIn && <Route exact path="/shopping/purchase-completed"><PurchaseCompleted/></Route>}
                {loggedIn && <Route exact path="/shopping/order-details/:id"><OrderDetails/></Route>}
                {loggedIn && <Route exact path="/shopping/find-user-orders"><FindUserOrders/></Route>}
                {loggedIn && <Route exact path="/shopping/find-company-orders"><FindCompanyOrders/></Route>}
                {loggedIn && <Route exact path="/shopping/find-user-orders-result"><FindUserOrdersResult/></Route>}
                {loggedIn && <Route exact path="/shopping/find-company-orders-result"><FindCompanyOrdersResult/></Route>}
                {loggedIn && <Route exact path="/shopping/find-discount-tickets-result"><FindDiscountTicketsResult/></Route>}
                {loggedIn && <Route exact path="/shopping/find-discount-tickets"><FindDiscountTickets/></Route>}
                {<Route exact path="/reservation/find-menu-products/:id/:reservationDate/:periodType/:diners"><FindMenuProducts/></Route>}
                {loggedIn && <Route exact path="/reservation/calculate-deposit/:id/:reservationDate/:periodType/:diners"><CalculateDeposit/></Route>}
                {loggedIn && <Route exact path="/reservation/reservation-details/:id/:reservationDate/:periodType/:diners"><ReservationDetails/></Route>}
                {loggedIn && <Route exact path="/reservation/reservation-completed"><ReservationCompleted/></Route>}
                {<Route exact path="/reservation/set-date-and-diners/:id"><SetDateAndDiners/></Route>}
                {loggedIn && <Route exact path="/reservation/reserve-details/:id"><ReserveDetails/></Route>}
                {loggedIn && <Route exact path="/reservation/find-user-reserves"><FindUserReserves/></Route>}
                {loggedIn && <Route exact path="/reservation/find-company-reserves/:reservationDate/:periodType"><FindCompanyReserves/></Route>}
                {loggedIn && <Route exact path="/reservation/find-user-reserves-result"><FindUserReservesResult/></Route>}
                {loggedIn && <Route exact path="/reservation/find-company-reserves-result/:reservationDate/:periodType"><FindCompanyReservesResult/></Route>}
                {loggedIn && <Route exact path="/reservation/set-criteria-for-company-reserves"><SetCriteriaForCompanyReserves/></Route>}
                {loggedIn && <Route exact path="/reservation/add-event-evaluation/:id"><AddEventEvaluation/></Route>}
                {loggedIn && <Route exact path="/reservation/find-user-event-evaluations"><FindUserEventEvaluations/></Route>}
                {loggedIn && <Route exact path="/reservation/find-user-event-evaluations-result"><FindUserEventEvaluationsResult/></Route>}
                {loggedIn && <Route exact path="/reservation/evaluation-completed"><EvaluationCompleted/></Route>}
                {loggedIn && <Route exact path="/businessCatalog/find-all-companies-result"><FindAllCompaniesResult/></Route>}
                {loggedIn && <Route exact path="/businessCatalog/find-all-companies-criteria"><FindAllCompaniesCriteria/></Route>}
                {loggedIn && <Route exact path="/business/find-company-categories-result"><FindCompanyCategoriesResult/></Route>}
                {loggedIn && <Route exact path="/management/find-product-categories-result"><FindProductCategoriesResult/></Route>}
                {loggedIn && <Route exact path="/business/find-cities-result"><FindCitiesResult/></Route>}
                {loggedIn && <Route exact path="/users/pay-fee"><PayFee/></Route>}
                {loggedIn && <Route exact path="/users/check-pay-fee"><CheckPayFee/></Route>}

                {!loggedIn && <Route exact path="/users/login"><Login/></Route>}
                {!loggedIn && <Route exact path="/users/signup"><SignUp/></Route>}
                {!loggedIn && <Route exact path="/users/signup-businessman"><SignUpBusinessman/></Route>}
                <Route><FindCompaniesByAddress/></Route>
            </Switch>
        </div>

    );

};

export default Body;
