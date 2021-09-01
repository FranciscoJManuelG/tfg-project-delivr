import React, {useState} from 'react';
import {FormattedMessage} from 'react-intl';
import PropTypes from 'prop-types';

import ListCompany from './ListCompany';
import {Errors} from '../../common';

const ListCompanies = ({companies, cities, onDeleteItem}) => {

    const [backendErrors, setBackendErrors] = useState(null);

    return (

        <div>

            <Errors errors={backendErrors}
                onClose={() => setBackendErrors(null)}/>

            <table className="table table-hover">

                <thead>
                    <tr>
                        <th scope="col">
                            <FormattedMessage id='project.global.fields.name'/>
                        </th>
                        <th scope="col">
                            <FormattedMessage id='project.global.fields.street'/>
                        </th>
                        <th scope="col">
                            <FormattedMessage id='project.global.fields.city'/>
                        </th>
                        <th scope="col" style={{width: '60%'}}></th>
                    </tr>
                </thead>

                <tbody>
                    {companies.map(company => 
                        <ListCompany key={company.id}
                            company={company}
                            cities={cities}
                            onDeleteItem={onDeleteItem}
                            onBackendErrors={errors => setBackendErrors(errors)}
                            />
                    )}
                </tbody>

            </table>
        </div>
    );
};

ListCompanies.propTypes = {
    companies: PropTypes.array.isRequired,
    cities: PropTypes.array.isRequired,
    onDeleteItem: PropTypes.func
};

export default ListCompanies;
