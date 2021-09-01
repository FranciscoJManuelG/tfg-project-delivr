import React, {useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {useParams} from 'react-router-dom';

import * as selectors from '../selectors';
import * as actions from '../actions';
import * as actionsBusiness from '../../business/actions';
import CompanyProductsToDeliver from './CompanyProductsToDeliver';
import FindProductsByCompany from './FindProductsByCompany';
import ShoppingCart from '../../shopping/components/ShoppingCart';
import BeginReserveLink from './BeginReserveLink';
import users from '../../users';

const FindProductsByCompanyToDeliverResult = () => {

    const userName = useSelector(users.selectors.getUserName);
    const productSearch = useSelector(selectors.getProductSearch);
    const productCategories = useSelector(selectors.getProductCategories);
    const dispatch = useDispatch();
    const {id, doReserve, cityId} = useParams();

    useEffect(() => {

        const companyId = Number(id);
        console.log(companyId);

        if (!Number.isNaN(companyId)) {
            dispatch(actionsBusiness.findCompanyById(companyId));
            dispatch(actions.findCompanyProductCategories(companyId));
            dispatch(actions.findProducts(companyId,
                {
                    productCategoryId: null,
                    keywords : ""
                }));   
                
        }
    }, [id, dispatch]);


    if (!productSearch) {
        return null;
    }

    if (productSearch.result.length === 0) {
        return (
            <div>
                {doReserve === "true" && <BeginReserveLink id={id}/>}
                <FindProductsByCompany/>
                <div className="alert alert-info" role="alert">
                    <FormattedMessage id='project.business.FindCompaniesResult.noCompanies'/>
                </div>
            </div>
        );
    }

    return (

        <div>
            {doReserve === "true" && <BeginReserveLink id={id}/>}
            {userName &&
            <FindProductsByCompany/>}
            <CompanyProductsToDeliver products={productSearch.result} productCategories={productCategories} companyId={id}/>
            {userName &&
            <ShoppingCart companyId={id} cityId={cityId}/>}
        </div>

    );

}

export default FindProductsByCompanyToDeliverResult;
