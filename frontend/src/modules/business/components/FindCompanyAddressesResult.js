import React from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {Link} from 'react-router-dom';
import {FormattedMessage} from 'react-intl';

import * as actions from '../actions';
import * as selectors from '../selectors';
import {Pager} from '../../common';
import CompanyAddresses from './CompanyAddresses';
import Sidebar from '../../common/components/BusinessSidebar'

const FindCompanyAddressesResult = () => {

    const company = useSelector(selectors.getCompany);
    const companyAddressSearch = useSelector(selectors.getCompanyAddressSearch);
    const cities = useSelector(selectors.getCities);
    const dispatch = useDispatch();

    if (!companyAddressSearch) {
        return null;
    }

    if (companyAddressSearch.result.items.length === 0) {
        return (
            <div>
                <Sidebar/>
                <div className="alert alert-info" role="alert">
                    <FormattedMessage id='project.business.FindCompanyAddressesResult.noCompanyAddresses'/>
                </div>
                <Link to='/business/add-company-address'>
                    <div className="form-group">
                        <button type="submit" className="btn btn-primary">Añadir</button>
                    </div> 
                </Link>
            </div>
        );
    }

    return (

        <div>
            <Sidebar/>
            <CompanyAddresses companyAddresses={companyAddressSearch.result.items} cities={cities}
                onDeleteItem={(...args) => dispatch(actions.deleteCompanyAddress(...args))}/>
            <Pager 
                back={{
                    enabled: companyAddressSearch.criteria.page >= 1,
                    onClick: () => dispatch(actions.previousFindCompanyAddressesResultPage(company.id, companyAddressSearch.criteria))}}
                next={{
                    enabled: companyAddressSearch.result.existMoreItems,
                    onClick: () => dispatch(actions.nextFindCompanyAddressesResultPage(company.id, companyAddressSearch.criteria))}}/>
            <Link to='/business/add-company-address'>
                <div className="vertical-center">
                    <button type="submit" className="btn btn-dark">Añadir</button>
                </div> 
            </Link>
        </div>

    );

}

export default FindCompanyAddressesResult;
