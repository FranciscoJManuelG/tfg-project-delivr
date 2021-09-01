import React from 'react';
import {FormattedMessage} from 'react-intl';
import PropTypes from 'prop-types';

import CompanyCategory from './CompanyCategory';

const CompanyCategories = ({companyCategories}) => {

    return (

        <div>

            <table className="table table-hover">

                <thead>
                    <tr>
                        <th scope="col">
                            <FormattedMessage id='project.global.fields.name'/>
                        </th>
                         <th scope="col" style={{width: '20%'}}></th>
                    </tr>
                </thead>

                <tbody>
                    {companyCategories.map(companyCategory => 
                        <CompanyCategory key={companyCategory.id}
                            companyCategory={companyCategory}
                            />
                    )}
                </tbody>

            </table>
        </div>
    );

}

CompanyCategories.propTypes = {
    companyCategories: PropTypes.array.isRequired,
};

export default CompanyCategories;

