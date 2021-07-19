import React from 'react';
import {useSelector} from 'react-redux';
import {Route, Switch} from 'react-router-dom';

import AppGlobalComponents from './AppGlobalComponents';
import {Login, SignUp, UpdateProfile, ChangePassword, Logout, SignUpBusinessman, AddFavouriteAddress, FindFavouriteAddresses, FindFavouriteAddressesResult} from '../../users';
import {AddCompany, ModifyCompany, FindCompanyAddresses, FindCompanyAddressesResult, AddCompanyAddress, StateCompany, AddGoal, FindGoals, FindGoalsResult, EditGoal, FindGoalToEdit} from '../../business';
import {FindCompaniesByAddress, FindCompaniesResult} from '../../businessCatalog';
import {FindProductsByCompanyResult} from '../../productCatalog';
import {FindProductsResult, FindProducts, AddProduct, EditProduct, FindProductToEdit} from '../../productManagement';
import {AddToShoppingCart, FindShoppingCartProducts, PurchaseDetails, SetAddressToSendPurchase, ShowFavAddresses, PurchaseCompleted, OrderDetails, FindUserOrdersResult, FindCompanyOrdersResult, FindCompanyOrders, FindUserOrders, FindDiscountTicketsResult, FindDiscountTickets} from '../../shopping';
import users from '../../users';

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
                {<Route exact path="/productCatalog/find-products-by-company-result/:id"><FindProductsByCompanyResult/></Route>}
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
                {loggedIn && <Route exact path="/shopping/add-to-shopping-cart"><AddToShoppingCart/></Route>}
                {loggedIn && <Route exact path="/shopping/find-shopping-cart-products/:id"><FindShoppingCartProducts/></Route>}
                {loggedIn && <Route exact path="/shopping/purchase-details/:id"><PurchaseDetails/></Route>}
                {loggedIn && <Route exact path="/shopping/set-address-to-send-purchase/:id"><SetAddressToSendPurchase/></Route>}
                {loggedIn && <Route exact path="/shopping/show-fav-addresses/:id"><ShowFavAddresses/></Route>}
                {loggedIn && <Route exact path="/shopping/purchase-completed"><PurchaseCompleted/></Route>}
                {loggedIn && <Route exact path="/shopping/order-details/:id"><OrderDetails/></Route>}
                {loggedIn && <Route exact path="/shopping/find-user-orders"><FindUserOrders/></Route>}
                {loggedIn && <Route exact path="/shopping/find-company-orders"><FindCompanyOrders/></Route>}
                {loggedIn && <Route exact path="/shopping/find-user-orders-result"><FindUserOrdersResult/></Route>}
                {loggedIn && <Route exact path="/shopping/find-company-orders-result"><FindCompanyOrdersResult/></Route>}
                {loggedIn && <Route exact path="/shopping/find-discount-tickets-result"><FindDiscountTicketsResult/></Route>}
                {loggedIn && <Route exact path="/shopping/find-discount-tickets"><FindDiscountTickets/></Route>}
                {!loggedIn && <Route exact path="/users/login"><Login/></Route>}
                {!loggedIn && <Route exact path="/users/signup"><SignUp/></Route>}
                {!loggedIn && <Route exact path="/users/signup-businessman"><SignUpBusinessman/></Route>}
                <Route><FindCompaniesByAddress/></Route>
            </Switch>
        </div>

    );

};

export default Body;
