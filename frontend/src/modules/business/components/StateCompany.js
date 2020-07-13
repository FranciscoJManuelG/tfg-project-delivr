import React from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {useHistory} from 'react-router-dom';

import * as actions from '../actions';
import * as selectors from '../selectors';
import Sidebar from '../../common/components/BusinessSidebar'

const StateCompany = () => {

    const company = useSelector(selectors.getCompany);
    const block = useSelector(selectors.getBlock);
    const history = useHistory();
    const dispatch = useDispatch();
    let form;
    

    const handleSubmitBlock = event => {

        event.preventDefault();
        
        dispatch(actions.blockCompany(company.id,
            () => history.push('/')));
    }

    const handleSubmitUnlock = event => {

        event.preventDefault();
        
        dispatch(actions.unlockCompany(company.id,
            () => history.push('/')));
    }
    
    return (

        <div className="container">
            <Sidebar/>
            {block === false ?
            
                <div>    
                    <div className="card bg-light border-dark">
                        <h1>Condiciones de bloqueo</h1>
                        <p>Si bloquea la compañía no aparecerá en la web al realizar la busqueda.</p>
                        <p>Si a los 30 días de haber bloqueado la compañía no se ha desbloqueado
                            , se eliminará del sistema tanto la compañía como la cuenta de usuario</p>
                        <p>En cualquier momento, después de haber bloqueado la compañía en 30 días podrás desbloquearla</p>
                    </div>
                    <form ref={node => form = node} className="needs-validation" 
                                noValidate onSubmit={e => handleSubmitBlock(e)}>
                        <div className="col">
                            <div className="form-group">
                                <button type="submit" className="btn btn-danger">Bloquear</button>
                            </div> 
                        </div>
                    </form>
                </div>

                :

                <div>    
                    <div className="card bg-light border-dark">
                        <h1>Condiciones de desbloqueo</h1>
                        <p>Puedde desbloquear la compañía para que le aparezcan a los usuarios en la web.</p>
                        <p>Si tarda más de 30 días desde que bloque la compañía, ésta se eliminará del sistema y la
                            cuenta asociada a ella </p>
                    </div>
                    <form ref={node => form = node} className="needs-validation" 
                                noValidate onSubmit={e => handleSubmitUnlock(e)}>
                        <div className="col">
                            <div className="form-group">
                                <button type="submit" className="btn btn-success">Desbloquear</button>
                            </div> 
                        </div>
                    </form>
                </div>

            }
        </div>

    );

}


export default StateCompany;
