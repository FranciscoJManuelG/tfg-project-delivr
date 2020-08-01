import React from 'react';
import {useHistory} from 'react-router-dom';
import {FormattedMessage} from 'react-intl';

const Cancel = () => {

    const history = useHistory();

    if (history.length <= 2) {
        return null;
    } 
    
    return (

        <button type="button" className="btn btn-danger float-left" 
            onClick={() => history.goBack()}>

            <FormattedMessage id='project.global.buttons.cancel'/>

        </button>

    );

};

export default Cancel;
