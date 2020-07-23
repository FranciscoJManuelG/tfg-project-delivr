import React, {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {FormattedMessage} from 'react-intl';

import CompanyProductCategorySelector from '../components/CompanyProductCategorySelector'
import * as actions from '../actions';
import * as businessSelectors from '../../business/selectors'

const FindProducts = () => {

    const company = useSelector(businessSelectors.getCompany);
    const dispatch = useDispatch();
    const [productCategoryId, setProductCategoryId] = useState('');
    const [keywords, setKeywords] = useState('');

    const handleSubmit = event => {
        event.preventDefault();
        
        dispatch(actions.findProducts(company.id,
            {
                productCategoryId : toNumber(productCategoryId),
                keywords: keywords.trim()
            }));
    }

    const toNumber = value => value.length > 0 ? Number(value) : null;

    return (

        <form className="form-inline" onSubmit={e => handleSubmit(e)}>

            <div className="card">
                <article className="card-body">
                    <CompanyProductCategorySelector id="productCategoryId" className="custom-select my-1 mr-sm-2"
                        value={productCategoryId} onChange={e => setProductCategoryId(e.target.value)}/>
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

export default FindProducts;
