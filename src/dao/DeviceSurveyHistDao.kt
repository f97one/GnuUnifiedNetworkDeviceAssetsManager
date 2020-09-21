package net.formula97.webapps.dao

import net.formula97.webapps.dao.config.DataSourceCreator
import net.formula97.webapps.entity.DeviceSurveyHist

class DeviceSurveyHistDao(): AbstractDao<DeviceSurveyHist>(dataSource = DataSourceCreator.getDataSource()) {
}