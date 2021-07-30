import React, {useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';
import {useParams} from 'react-router-dom';

import * as selectors from '../selectors';
import * as actions from '../actions';
import CompanyProductsForReservations from './CompanyProductsForReservations';
import FindProductsByCompany from './FindProductsByCompany';
import Menu from '../../reservation/components/Menu';

const FindProductsByCompanyForReservationsResult = () => {

    const productSearch = useSelector(selectors.getProductSearch);
    const productCategories = useSelector(selectors.getProductCategories);
    const dispatch = useDispatch();
    const {id, reservationDate, periodType, diners} = useParams();

    useEffect(() => {

        const companyId = Number(id);

        if (!Number.isNaN(companyId)) {
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
                <FindProductsByCompany/>
                <div className="alert alert-info" role="alert">
                    <FormattedMessage id='project.business.FindCompaniesResult.noCompanies'/>
                </div>
            </div>
        );
    }

    return (

        <div>
            <FindProductsByCompany/>
            <CompanyProductsForReservations products={productSearch.result} productCategories={productCategories} companyId={id}/>
            <Menu companyId={id} reservationDate={reservationDate} periodType={periodType} diners={diners}/>
        </div>

    );

}

export default FindProductsByCompanyForReservationsResult;
