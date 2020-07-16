import React from 'react';
import {useSelector} from 'react-redux';
import {Route, Switch} from 'react-router-dom';

import AppGlobalComponents from './AppGlobalComponents';
import {Login, SignUp, UpdateProfile, ChangePassword, Logout, SignUpBusinessman, AddFavouriteAddress, FindFavouriteAddresses, FindFavouriteAddressesResult} from '../../users';
import {AddCompany, ModifyCompany, FindCompanyAddresses, FindCompanyAddressesResult, AddCompanyAddress, StateCompany} from '../../business';
import {FindCompaniesByAddress, FindCompaniesResult} from '../../businessCatalog';
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
                {!loggedIn && <Route exact path="/users/login"><Login/></Route>}
                {!loggedIn && <Route exact path="/users/signup"><SignUp/></Route>}
                {!loggedIn && <Route exact path="/users/signup-businessman"><SignUpBusinessman/></Route>}
                <Route><FindCompaniesByAddress/></Route>
            </Switch>
        </div>

    );

};

export default Body;
