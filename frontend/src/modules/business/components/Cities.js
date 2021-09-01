import React from 'react';
import {FormattedMessage} from 'react-intl';
import PropTypes from 'prop-types';

import City from './City';

const Cities = ({cities}) => {

    return (

        <div>

            <table className="table table-hover">

                <thead>
                    <tr>
                        <th scope="col">
                            <FormattedMessage id='project.global.fields.name'/>
                        </th>
                        <th scope="col">
                            Provincia
                        </th>
                        <th scope="col" style={{width: '20%'}}></th>
                    </tr>
                </thead>

                <tbody>
                    {cities.map(city => 
                        <City key={city.id}
                            city={city}
                            />
                    )}
                </tbody>

            </table>
        </div>
    );

}

Cities.propTypes = {
    cities: PropTypes.array.isRequired,
};

export default Cities;

