import React from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';

import * as selectors from '../selectors';
import * as actions from '../actions';
import Companies from './Companies';
import FindCompanies from './FindCompanies';
import * as businessSelectors from '../../business/selectors'
import {Pager} from '../../common';

const FindCompaniesResult = () => {

    const companySearch = useSelector(selectors.getCompanySearch);
    const companyCategories = useSelector(businessSelectors.getCompanyCategories);
    const cities = useSelector(businessSelectors.getCities);
    const dispatch = useDispatch();

    if (!companySearch) {
        return null;
    }

    if (companySearch.result.items.length === 0) {
        return (
            <div>
                <FindCompanies/>
                <div className="alert alert-info" role="alert">
                    <FormattedMessage id='project.business.FindCompaniesResult.noCompanies'/>
                </div>
            </div>
        );
    }

    return (

        <div>
            <FindCompanies/>
            <Companies companies={companySearch.result.items} companyCategories={companyCategories} cities={cities}/>
            <Pager 
                back={{
                    enabled: companySearch.criteria.page >= 1,
                    onClick: () => dispatch(actions.previousFindCompaniesResultPage(companySearch.criteria))}}
                next={{
                    enabled: companySearch.result.existMoreItems,
                    onClick: () => dispatch(actions.nextFindCompaniesResultPage(companySearch.criteria))}}/>
        </div>

    );

}

export default FindCompaniesResult;
