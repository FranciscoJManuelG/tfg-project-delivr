import React, {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {FormattedMessage} from 'react-intl';

import CompanyCategorySelector from '../../business/components/CompanyCategorySelector';
import * as actions from '../actions';
import * as selectors from '../selectors';

const FindCompanies = () => {

    const companySearch = useSelector(selectors.getCompanySearch);
    const dispatch = useDispatch();
    const [companyCategoryId, setCompanyCategoryId] = useState('');
    const [cityId] = useState(companySearch.criteria.cityId);
    const [street] = useState(companySearch.criteria.street);
    const [keywords, setKeywords] = useState('');

    const handleSubmit = event => {
        event.preventDefault();

        dispatch(actions.findCompanies(
            {companyCategoryId: toNumber(companyCategoryId),
            cityId: cityId,
            street: street.trim(), 
            keywords: keywords.trim(), 
            page: 0}));

    }

    const toNumber = value => value.length > 0 ? Number(value) : null;

    return (

        <form className="form-inline justify-content-center" onSubmit={e => handleSubmit(e)}>

            <div className="card">
                <article className="card-body">
                    <CompanyCategorySelector id="companyCategoryId" className="custom-select my-1 mr-sm-2"
                        value={companyCategoryId} onChange={e => setCompanyCategoryId(e.target.value)}/>
                    <input id="keywords" placeholder="Nombre del restaurante" type="text" className="form-control mr-sm-2"
                        value={keywords} onChange={e => setKeywords(e.target.value)}/>
                    
                    <button type="submit" className="btn btn-primary my-2 my-sm-0">
                        <FormattedMessage id='project.global.buttons.search'/>
                    </button>
                </article>
            </div>
        </form>

    );

}

export default FindCompanies;
