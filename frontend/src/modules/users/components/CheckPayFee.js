import {useEffect} from 'react';
import {useSelector} from 'react-redux';
import {useHistory} from 'react-router-dom';

import * as selectors from '../selectors';

const CheckPayFee = () => {

    const user = useSelector(selectors.getUser);
    const history = useHistory();

    useEffect(() => {

        {!user.feePaid && user.role === "BUSINESSMAN" ? 
            history.push('/users/pay-fee')
        :
            history.push('/');
        }
    
    });

    return null;

}

export default CheckPayFee;
