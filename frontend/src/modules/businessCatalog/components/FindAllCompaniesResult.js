import React from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {FormattedMessage} from 'react-intl';

import * as selectors from '../selectors';
import * as actions from '../actions';
import * as actionsBusiness from '../../business/actions';

import ListCompanies from './ListCompanies';
import FindAllCompanies from './FindAllCompanies';
import * as businessSelectors from '../../business/selectors'
import {Pager} from '../../common';

const FindAllCompaniesResult = () => {

    const companySearch = useSelector(selectors.getCompanySearch);
    const cities = useSelector(businessSelectors.getCities);
    const dispatch = useDispatch();

    if (!companySearch) {
        return null;
    }

    if (companySearch.result.items.length === 0) {
        return (
            <div>
                <FindAllCompanies/>
                <div className="alert alert-info" role="alert">
                    <FormattedMessage id='project.business.FindCompaniesResult.noCompanies'/>
                </div>
            </div>
        );
    }

    return (

        <div>
            <FindAllCompanies/>
            <ListCompanies companies={companySearch.result.items} cities={cities}
            onDeleteItem={(...args) => dispatch(actionsBusiness.deleteCompany(...args))}/>
            <Pager 
                back={{
                    enabled: companySearch.criteria.page >= 1,
                    onClick: () => dispatch(actions.previousFindAllCompaniesResultPage(companySearch.criteria))}}
                next={{
                    enabled: companySearch.result.existMoreItems,
                    onClick: () => dispatch(actions.nextFindAllCompaniesResultPage(companySearch.criteria))}}/>
        </div>

    );

}

export default FindAllCompaniesResult;
