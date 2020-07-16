import React from 'react';
import {FormattedMessage} from 'react-intl';
import PropTypes from 'prop-types';

import * as businessSelectors from '../../business/selectors';

const Companies = ({companies, companyCategories, cities}) => (

    <div className="container-fluid">
        {companies.map(company => 
                <div key={company.id} className="card col-lg-5 mt-3 mr-2 ml-2" style={{display:'inline-block'}}>
                    <div className="card-body">
                        <h4 className="card-title">{company.name}</h4>
                        <h6 className="card-subtitle mb-2 text-muted">{businessSelectors.getCompanyCategoryName(companyCategories, company.companyCategoryId)}</h6>
                        <p className="card-text">
                            {company.street}, {company.cp}, {businessSelectors.getCityName(cities, company.cityId)}
                        </p>
                        <div className="justify-content-between align-items-center">
                            <div>
                                <p>Capacidad: {company.capacity}</p>
                                <p>Porcentage de reserva: {company.reservePercentage}</p>
                            </div>
                        </div>
                    </div>
                </div>
        )}
    </div>

);

Companies.propTypes = {
    companies: PropTypes.array.isRequired,
    companyCategories: PropTypes.array.isRequired
};

export default Companies;
