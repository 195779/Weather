package com.example.weather.gson;

import java.util.List;

public class AppData {

    private int ret;
    private String info;
    private DataDTO data;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public static class DataDTO {
        private Diseaseh5ShelfDTO diseaseh5Shelf;
        private List<LocalCityNCOVDataListDTO> localCityNCOVDataList;

        public Diseaseh5ShelfDTO getDiseaseh5Shelf() {
            return diseaseh5Shelf;
        }

        public void setDiseaseh5Shelf(Diseaseh5ShelfDTO diseaseh5Shelf) {
            this.diseaseh5Shelf = diseaseh5Shelf;
        }

        public List<LocalCityNCOVDataListDTO> getLocalCityNCOVDataList() {
            return localCityNCOVDataList;
        }

        public void setLocalCityNCOVDataList(List<LocalCityNCOVDataListDTO> localCityNCOVDataList) {
            this.localCityNCOVDataList = localCityNCOVDataList;
        }

        public static class Diseaseh5ShelfDTO {
            private String lastUpdateTime;
            private ChinaTotalDTO chinaTotal;
            private ChinaAddDTO chinaAdd;
            private boolean isShowAdd;
            private ShowAddSwitchDTO showAddSwitch;
            private List<AreaTreeDTO> areaTree;

            public String getLastUpdateTime() {
                return lastUpdateTime;
            }

            public void setLastUpdateTime(String lastUpdateTime) {
                this.lastUpdateTime = lastUpdateTime;
            }

            public ChinaTotalDTO getChinaTotal() {
                return chinaTotal;
            }

            public void setChinaTotal(ChinaTotalDTO chinaTotal) {
                this.chinaTotal = chinaTotal;
            }

            public ChinaAddDTO getChinaAdd() {
                return chinaAdd;
            }

            public void setChinaAdd(ChinaAddDTO chinaAdd) {
                this.chinaAdd = chinaAdd;
            }

            public boolean isIsShowAdd() {
                return isShowAdd;
            }

            public void setIsShowAdd(boolean isShowAdd) {
                this.isShowAdd = isShowAdd;
            }

            public ShowAddSwitchDTO getShowAddSwitch() {
                return showAddSwitch;
            }

            public void setShowAddSwitch(ShowAddSwitchDTO showAddSwitch) {
                this.showAddSwitch = showAddSwitch;
            }

            public List<AreaTreeDTO> getAreaTree() {
                return areaTree;
            }

            public void setAreaTree(List<AreaTreeDTO> areaTree) {
                this.areaTree = areaTree;
            }

            public static class ChinaTotalDTO {
                private int highRiskAreaNum;
                private int showlocalinfeciton;
                private int deadAdd;
                private int mediumRiskAreaNum;
                private int heal;
                private int suspect;
                private int nowSevere;
                private int localConfirmH5;
                private int local_acc_confirm;
                private int confirmAdd;
                private int nowConfirm;
                private int localConfirm;
                private int importedCase;
                private int showLocalConfirm;
                private int noInfectH5;
                private String mRiskTime;
                private int localWzzAdd;
                private int nowLocalWzz;
                private String mtime;
                private int localConfirmAdd;
                private int confirm;
                private int dead;
                private int noInfect;

                public int getHighRiskAreaNum() {
                    return highRiskAreaNum;
                }

                public void setHighRiskAreaNum(int highRiskAreaNum) {
                    this.highRiskAreaNum = highRiskAreaNum;
                }

                public int getShowlocalinfeciton() {
                    return showlocalinfeciton;
                }

                public void setShowlocalinfeciton(int showlocalinfeciton) {
                    this.showlocalinfeciton = showlocalinfeciton;
                }

                public int getDeadAdd() {
                    return deadAdd;
                }

                public void setDeadAdd(int deadAdd) {
                    this.deadAdd = deadAdd;
                }

                public int getMediumRiskAreaNum() {
                    return mediumRiskAreaNum;
                }

                public void setMediumRiskAreaNum(int mediumRiskAreaNum) {
                    this.mediumRiskAreaNum = mediumRiskAreaNum;
                }

                public int getHeal() {
                    return heal;
                }

                public void setHeal(int heal) {
                    this.heal = heal;
                }

                public int getSuspect() {
                    return suspect;
                }

                public void setSuspect(int suspect) {
                    this.suspect = suspect;
                }

                public int getNowSevere() {
                    return nowSevere;
                }

                public void setNowSevere(int nowSevere) {
                    this.nowSevere = nowSevere;
                }

                public int getLocalConfirmH5() {
                    return localConfirmH5;
                }

                public void setLocalConfirmH5(int localConfirmH5) {
                    this.localConfirmH5 = localConfirmH5;
                }

                public int getLocal_acc_confirm() {
                    return local_acc_confirm;
                }

                public void setLocal_acc_confirm(int local_acc_confirm) {
                    this.local_acc_confirm = local_acc_confirm;
                }

                public int getConfirmAdd() {
                    return confirmAdd;
                }

                public void setConfirmAdd(int confirmAdd) {
                    this.confirmAdd = confirmAdd;
                }

                public int getNowConfirm() {
                    return nowConfirm;
                }

                public void setNowConfirm(int nowConfirm) {
                    this.nowConfirm = nowConfirm;
                }

                public int getLocalConfirm() {
                    return localConfirm;
                }

                public void setLocalConfirm(int localConfirm) {
                    this.localConfirm = localConfirm;
                }

                public int getImportedCase() {
                    return importedCase;
                }

                public void setImportedCase(int importedCase) {
                    this.importedCase = importedCase;
                }

                public int getShowLocalConfirm() {
                    return showLocalConfirm;
                }

                public void setShowLocalConfirm(int showLocalConfirm) {
                    this.showLocalConfirm = showLocalConfirm;
                }

                public int getNoInfectH5() {
                    return noInfectH5;
                }

                public void setNoInfectH5(int noInfectH5) {
                    this.noInfectH5 = noInfectH5;
                }

                public String getMRiskTime() {
                    return mRiskTime;
                }

                public void setMRiskTime(String mRiskTime) {
                    this.mRiskTime = mRiskTime;
                }

                public int getLocalWzzAdd() {
                    return localWzzAdd;
                }

                public void setLocalWzzAdd(int localWzzAdd) {
                    this.localWzzAdd = localWzzAdd;
                }

                public int getNowLocalWzz() {
                    return nowLocalWzz;
                }

                public void setNowLocalWzz(int nowLocalWzz) {
                    this.nowLocalWzz = nowLocalWzz;
                }

                public String getMtime() {
                    return mtime;
                }

                public void setMtime(String mtime) {
                    this.mtime = mtime;
                }

                public int getLocalConfirmAdd() {
                    return localConfirmAdd;
                }

                public void setLocalConfirmAdd(int localConfirmAdd) {
                    this.localConfirmAdd = localConfirmAdd;
                }

                public int getConfirm() {
                    return confirm;
                }

                public void setConfirm(int confirm) {
                    this.confirm = confirm;
                }

                public int getDead() {
                    return dead;
                }

                public void setDead(int dead) {
                    this.dead = dead;
                }

                public int getNoInfect() {
                    return noInfect;
                }

                public void setNoInfect(int noInfect) {
                    this.noInfect = noInfect;
                }
            }

            public static class ChinaAddDTO {
                private int localConfirmH5;
                private int dead;
                private int nowConfirm;
                private int suspect;
                private int importedCase;
                private int localConfirm;
                private int noInfectH5;
                private int confirm;
                private int heal;
                private int nowSevere;
                private int noInfect;

                public int getLocalConfirmH5() {
                    return localConfirmH5;
                }

                public void setLocalConfirmH5(int localConfirmH5) {
                    this.localConfirmH5 = localConfirmH5;
                }

                public int getDead() {
                    return dead;
                }

                public void setDead(int dead) {
                    this.dead = dead;
                }

                public int getNowConfirm() {
                    return nowConfirm;
                }

                public void setNowConfirm(int nowConfirm) {
                    this.nowConfirm = nowConfirm;
                }

                public int getSuspect() {
                    return suspect;
                }

                public void setSuspect(int suspect) {
                    this.suspect = suspect;
                }

                public int getImportedCase() {
                    return importedCase;
                }

                public void setImportedCase(int importedCase) {
                    this.importedCase = importedCase;
                }

                public int getLocalConfirm() {
                    return localConfirm;
                }

                public void setLocalConfirm(int localConfirm) {
                    this.localConfirm = localConfirm;
                }

                public int getNoInfectH5() {
                    return noInfectH5;
                }

                public void setNoInfectH5(int noInfectH5) {
                    this.noInfectH5 = noInfectH5;
                }

                public int getConfirm() {
                    return confirm;
                }

                public void setConfirm(int confirm) {
                    this.confirm = confirm;
                }

                public int getHeal() {
                    return heal;
                }

                public void setHeal(int heal) {
                    this.heal = heal;
                }

                public int getNowSevere() {
                    return nowSevere;
                }

                public void setNowSevere(int nowSevere) {
                    this.nowSevere = nowSevere;
                }

                public int getNoInfect() {
                    return noInfect;
                }

                public void setNoInfect(int noInfect) {
                    this.noInfect = noInfect;
                }
            }

            public static class ShowAddSwitchDTO {
                private boolean all;
                private boolean confirm;
                private boolean importedCase;
                private boolean localConfirm;
                private boolean localinfeciton;
                private boolean suspect;
                private boolean dead;
                private boolean heal;
                private boolean nowConfirm;
                private boolean nowSevere;
                private boolean noInfect;

                public boolean isAll() {
                    return all;
                }

                public void setAll(boolean all) {
                    this.all = all;
                }

                public boolean isConfirm() {
                    return confirm;
                }

                public void setConfirm(boolean confirm) {
                    this.confirm = confirm;
                }

                public boolean isImportedCase() {
                    return importedCase;
                }

                public void setImportedCase(boolean importedCase) {
                    this.importedCase = importedCase;
                }

                public boolean isLocalConfirm() {
                    return localConfirm;
                }

                public void setLocalConfirm(boolean localConfirm) {
                    this.localConfirm = localConfirm;
                }

                public boolean isLocalinfeciton() {
                    return localinfeciton;
                }

                public void setLocalinfeciton(boolean localinfeciton) {
                    this.localinfeciton = localinfeciton;
                }

                public boolean isSuspect() {
                    return suspect;
                }

                public void setSuspect(boolean suspect) {
                    this.suspect = suspect;
                }

                public boolean isDead() {
                    return dead;
                }

                public void setDead(boolean dead) {
                    this.dead = dead;
                }

                public boolean isHeal() {
                    return heal;
                }

                public void setHeal(boolean heal) {
                    this.heal = heal;
                }

                public boolean isNowConfirm() {
                    return nowConfirm;
                }

                public void setNowConfirm(boolean nowConfirm) {
                    this.nowConfirm = nowConfirm;
                }

                public boolean isNowSevere() {
                    return nowSevere;
                }

                public void setNowSevere(boolean nowSevere) {
                    this.nowSevere = nowSevere;
                }

                public boolean isNoInfect() {
                    return noInfect;
                }

                public void setNoInfect(boolean noInfect) {
                    this.noInfect = noInfect;
                }
            }

            public static class AreaTreeDTO {
                private TodayDTO today;
                private TotalDTO total;
                private List<ChildrenDTO> children;
                private String name;

                public TodayDTO getToday() {
                    return today;
                }

                public void setToday(TodayDTO today) {
                    this.today = today;
                }

                public TotalDTO getTotal() {
                    return total;
                }

                public void setTotal(TotalDTO total) {
                    this.total = total;
                }

                public List<ChildrenDTO> getChildren() {
                    return children;
                }

                public void setChildren(List<ChildrenDTO> children) {
                    this.children = children;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public static class TodayDTO {
                    private int confirm;
                    private boolean isUpdated;

                    public int getConfirm() {
                        return confirm;
                    }

                    public void setConfirm(int confirm) {
                        this.confirm = confirm;
                    }

                    public boolean isIsUpdated() {
                        return isUpdated;
                    }

                    public void setIsUpdated(boolean isUpdated) {
                        this.isUpdated = isUpdated;
                    }
                }

                public static class TotalDTO {
                    private int continueDayZeroLocalConfirmAdd;
                    private String mtime;
                    private int heal;
                    private int mediumRiskAreaNum;
                    private int wzz;
                    private int provinceLocalConfirm;
                    private int highRiskAreaNum;
                    private int nowConfirm;
                    private boolean showHeal;
                    private boolean showRate;
                    private int continueDayZeroLocalConfirm;
                    private String adcode;
                    private int confirm;
                    private int dead;

                    public int getContinueDayZeroLocalConfirmAdd() {
                        return continueDayZeroLocalConfirmAdd;
                    }

                    public void setContinueDayZeroLocalConfirmAdd(int continueDayZeroLocalConfirmAdd) {
                        this.continueDayZeroLocalConfirmAdd = continueDayZeroLocalConfirmAdd;
                    }

                    public String getMtime() {
                        return mtime;
                    }

                    public void setMtime(String mtime) {
                        this.mtime = mtime;
                    }

                    public int getHeal() {
                        return heal;
                    }

                    public void setHeal(int heal) {
                        this.heal = heal;
                    }

                    public int getMediumRiskAreaNum() {
                        return mediumRiskAreaNum;
                    }

                    public void setMediumRiskAreaNum(int mediumRiskAreaNum) {
                        this.mediumRiskAreaNum = mediumRiskAreaNum;
                    }

                    public int getWzz() {
                        return wzz;
                    }

                    public void setWzz(int wzz) {
                        this.wzz = wzz;
                    }

                    public int getProvinceLocalConfirm() {
                        return provinceLocalConfirm;
                    }

                    public void setProvinceLocalConfirm(int provinceLocalConfirm) {
                        this.provinceLocalConfirm = provinceLocalConfirm;
                    }

                    public int getHighRiskAreaNum() {
                        return highRiskAreaNum;
                    }

                    public void setHighRiskAreaNum(int highRiskAreaNum) {
                        this.highRiskAreaNum = highRiskAreaNum;
                    }

                    public int getNowConfirm() {
                        return nowConfirm;
                    }

                    public void setNowConfirm(int nowConfirm) {
                        this.nowConfirm = nowConfirm;
                    }

                    public boolean isShowHeal() {
                        return showHeal;
                    }

                    public void setShowHeal(boolean showHeal) {
                        this.showHeal = showHeal;
                    }

                    public boolean isShowRate() {
                        return showRate;
                    }

                    public void setShowRate(boolean showRate) {
                        this.showRate = showRate;
                    }

                    public int getContinueDayZeroLocalConfirm() {
                        return continueDayZeroLocalConfirm;
                    }

                    public void setContinueDayZeroLocalConfirm(int continueDayZeroLocalConfirm) {
                        this.continueDayZeroLocalConfirm = continueDayZeroLocalConfirm;
                    }

                    public String getAdcode() {
                        return adcode;
                    }

                    public void setAdcode(String adcode) {
                        this.adcode = adcode;
                    }

                    public int getConfirm() {
                        return confirm;
                    }

                    public void setConfirm(int confirm) {
                        this.confirm = confirm;
                    }

                    public int getDead() {
                        return dead;
                    }

                    public void setDead(int dead) {
                        this.dead = dead;
                    }
                }

                public static class ChildrenDTO {
                    private String adcode;
                    private String date;
                    private TodayDTO today;
                    private TotalDTO total;
                    private List<ChildrenDTO> children;
                    private String name;

                    public String getAdcode() {
                        return adcode;
                    }

                    public void setAdcode(String adcode) {
                        this.adcode = adcode;
                    }

                    public String getDate() {
                        return date;
                    }

                    public void setDate(String date) {
                        this.date = date;
                    }

                    public TodayDTO getToday() {
                        return today;
                    }

                    public void setToday(TodayDTO today) {
                        this.today = today;
                    }

                    public TotalDTO getTotal() {
                        return total;
                    }

                    public void setTotal(TotalDTO total) {
                        this.total = total;
                    }

                    public List<ChildrenDTO> getChildren() {
                        return children;
                    }

                    public void setChildren(List<ChildrenDTO> children) {
                        this.children = children;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public static class TodayDTO {
                        private int confirmCuts;
                        private boolean isUpdated;
                        private String tip;
                        private int wzz_add;
                        private int local_confirm_add;
                        private int abroad_confirm_add;
                        private int dead_add;
                        private int confirm;

                        public int getConfirmCuts() {
                            return confirmCuts;
                        }

                        public void setConfirmCuts(int confirmCuts) {
                            this.confirmCuts = confirmCuts;
                        }

                        public boolean isIsUpdated() {
                            return isUpdated;
                        }

                        public void setIsUpdated(boolean isUpdated) {
                            this.isUpdated = isUpdated;
                        }

                        public String getTip() {
                            return tip;
                        }

                        public void setTip(String tip) {
                            this.tip = tip;
                        }

                        public int getWzz_add() {
                            return wzz_add;
                        }

                        public void setWzz_add(int wzz_add) {
                            this.wzz_add = wzz_add;
                        }

                        public int getLocal_confirm_add() {
                            return local_confirm_add;
                        }

                        public void setLocal_confirm_add(int local_confirm_add) {
                            this.local_confirm_add = local_confirm_add;
                        }

                        public int getAbroad_confirm_add() {
                            return abroad_confirm_add;
                        }

                        public void setAbroad_confirm_add(int abroad_confirm_add) {
                            this.abroad_confirm_add = abroad_confirm_add;
                        }

                        public int getDead_add() {
                            return dead_add;
                        }

                        public void setDead_add(int dead_add) {
                            this.dead_add = dead_add;
                        }

                        public int getConfirm() {
                            return confirm;
                        }

                        public void setConfirm(int confirm) {
                            this.confirm = confirm;
                        }
                    }

                    public static class TotalDTO {
                        private int mediumRiskAreaNum;
                        private int continueDayZeroConfirmAdd;
                        private int wzz;
                        private int dead;
                        private boolean showHeal;
                        private String adcode;
                        private int confirm;
                        private int heal;
                        private int provinceLocalConfirm;
                        private String mtime;
                        private int nowConfirm;
                        private int highRiskAreaNum;
                        private int continueDayZeroConfirm;
                        private int continueDayZeroLocalConfirmAdd;
                        private boolean showRate;

                        public int getMediumRiskAreaNum() {
                            return mediumRiskAreaNum;
                        }

                        public void setMediumRiskAreaNum(int mediumRiskAreaNum) {
                            this.mediumRiskAreaNum = mediumRiskAreaNum;
                        }

                        public int getContinueDayZeroConfirmAdd() {
                            return continueDayZeroConfirmAdd;
                        }

                        public void setContinueDayZeroConfirmAdd(int continueDayZeroConfirmAdd) {
                            this.continueDayZeroConfirmAdd = continueDayZeroConfirmAdd;
                        }

                        public int getWzz() {
                            return wzz;
                        }

                        public void setWzz(int wzz) {
                            this.wzz = wzz;
                        }

                        public int getDead() {
                            return dead;
                        }

                        public void setDead(int dead) {
                            this.dead = dead;
                        }

                        public boolean isShowHeal() {
                            return showHeal;
                        }

                        public void setShowHeal(boolean showHeal) {
                            this.showHeal = showHeal;
                        }

                        public String getAdcode() {
                            return adcode;
                        }

                        public void setAdcode(String adcode) {
                            this.adcode = adcode;
                        }

                        public int getConfirm() {
                            return confirm;
                        }

                        public void setConfirm(int confirm) {
                            this.confirm = confirm;
                        }

                        public int getHeal() {
                            return heal;
                        }

                        public void setHeal(int heal) {
                            this.heal = heal;
                        }

                        public int getProvinceLocalConfirm() {
                            return provinceLocalConfirm;
                        }

                        public void setProvinceLocalConfirm(int provinceLocalConfirm) {
                            this.provinceLocalConfirm = provinceLocalConfirm;
                        }

                        public String getMtime() {
                            return mtime;
                        }

                        public void setMtime(String mtime) {
                            this.mtime = mtime;
                        }

                        public int getNowConfirm() {
                            return nowConfirm;
                        }

                        public void setNowConfirm(int nowConfirm) {
                            this.nowConfirm = nowConfirm;
                        }

                        public int getHighRiskAreaNum() {
                            return highRiskAreaNum;
                        }

                        public void setHighRiskAreaNum(int highRiskAreaNum) {
                            this.highRiskAreaNum = highRiskAreaNum;
                        }

                        public int getContinueDayZeroConfirm() {
                            return continueDayZeroConfirm;
                        }

                        public void setContinueDayZeroConfirm(int continueDayZeroConfirm) {
                            this.continueDayZeroConfirm = continueDayZeroConfirm;
                        }

                        public int getContinueDayZeroLocalConfirmAdd() {
                            return continueDayZeroLocalConfirmAdd;
                        }

                        public void setContinueDayZeroLocalConfirmAdd(int continueDayZeroLocalConfirmAdd) {
                            this.continueDayZeroLocalConfirmAdd = continueDayZeroLocalConfirmAdd;
                        }

                        public boolean isShowRate() {
                            return showRate;
                        }

                        public void setShowRate(boolean showRate) {
                            this.showRate = showRate;
                        }
                    }
//重复内部类名字
                    public static class ChildrenDTO01 {
                        private String date;
                        private TodayDTO today;
                        private TotalDTO total;
                        private String name;
                        private String adcode;

                        public String getDate() {
                            return date;
                        }

                        public void setDate(String date) {
                            this.date = date;
                        }

                        public TodayDTO getToday() {
                            return today;
                        }

                        public void setToday(TodayDTO today) {
                            this.today = today;
                        }

                        public TotalDTO getTotal() {
                            return total;
                        }

                        public void setTotal(TotalDTO total) {
                            this.total = total;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                        public String getAdcode() {
                            return adcode;
                        }

                        public void setAdcode(String adcode) {
                            this.adcode = adcode;
                        }

                        public static class TodayDTO {
                            private int confirm;
                            private int confirmCuts;
                            private boolean isUpdated;
                            private String wzz_add;
                            private int local_confirm_add;

                            public int getConfirm() {
                                return confirm;
                            }

                            public void setConfirm(int confirm) {
                                this.confirm = confirm;
                            }

                            public int getConfirmCuts() {
                                return confirmCuts;
                            }

                            public void setConfirmCuts(int confirmCuts) {
                                this.confirmCuts = confirmCuts;
                            }

                            public boolean isIsUpdated() {
                                return isUpdated;
                            }

                            public void setIsUpdated(boolean isUpdated) {
                                this.isUpdated = isUpdated;
                            }

                            public String getWzz_add() {
                                return wzz_add;
                            }

                            public void setWzz_add(String wzz_add) {
                                this.wzz_add = wzz_add;
                            }

                            public int getLocal_confirm_add() {
                                return local_confirm_add;
                            }

                            public void setLocal_confirm_add(int local_confirm_add) {
                                this.local_confirm_add = local_confirm_add;
                            }
                        }

                        public static class TotalDTO {
                            private int highRiskAreaNum;
                            private String adcode;
                            private int nowConfirm;
                            private int confirm;
                            private int dead;
                            private boolean showHeal;
                            private int provinceLocalConfirm;
                            private int continueDayZeroLocalConfirmAdd;
                            private int continueDayZeroLocalConfirm;
                            private String mtime;
                            private boolean showRate;
                            private int heal;
                            private int wzz;
                            private int mediumRiskAreaNum;

                            public int getHighRiskAreaNum() {
                                return highRiskAreaNum;
                            }

                            public void setHighRiskAreaNum(int highRiskAreaNum) {
                                this.highRiskAreaNum = highRiskAreaNum;
                            }

                            public String getAdcode() {
                                return adcode;
                            }

                            public void setAdcode(String adcode) {
                                this.adcode = adcode;
                            }

                            public int getNowConfirm() {
                                return nowConfirm;
                            }

                            public void setNowConfirm(int nowConfirm) {
                                this.nowConfirm = nowConfirm;
                            }

                            public int getConfirm() {
                                return confirm;
                            }

                            public void setConfirm(int confirm) {
                                this.confirm = confirm;
                            }

                            public int getDead() {
                                return dead;
                            }

                            public void setDead(int dead) {
                                this.dead = dead;
                            }

                            public boolean isShowHeal() {
                                return showHeal;
                            }

                            public void setShowHeal(boolean showHeal) {
                                this.showHeal = showHeal;
                            }

                            public int getProvinceLocalConfirm() {
                                return provinceLocalConfirm;
                            }

                            public void setProvinceLocalConfirm(int provinceLocalConfirm) {
                                this.provinceLocalConfirm = provinceLocalConfirm;
                            }

                            public int getContinueDayZeroLocalConfirmAdd() {
                                return continueDayZeroLocalConfirmAdd;
                            }

                            public void setContinueDayZeroLocalConfirmAdd(int continueDayZeroLocalConfirmAdd) {
                                this.continueDayZeroLocalConfirmAdd = continueDayZeroLocalConfirmAdd;
                            }

                            public int getContinueDayZeroLocalConfirm() {
                                return continueDayZeroLocalConfirm;
                            }

                            public void setContinueDayZeroLocalConfirm(int continueDayZeroLocalConfirm) {
                                this.continueDayZeroLocalConfirm = continueDayZeroLocalConfirm;
                            }

                            public String getMtime() {
                                return mtime;
                            }

                            public void setMtime(String mtime) {
                                this.mtime = mtime;
                            }

                            public boolean isShowRate() {
                                return showRate;
                            }

                            public void setShowRate(boolean showRate) {
                                this.showRate = showRate;
                            }

                            public int getHeal() {
                                return heal;
                            }

                            public void setHeal(int heal) {
                                this.heal = heal;
                            }

                            public int getWzz() {
                                return wzz;
                            }

                            public void setWzz(int wzz) {
                                this.wzz = wzz;
                            }

                            public int getMediumRiskAreaNum() {
                                return mediumRiskAreaNum;
                            }

                            public void setMediumRiskAreaNum(int mediumRiskAreaNum) {
                                this.mediumRiskAreaNum = mediumRiskAreaNum;
                            }
                        }
                    }
                }
            }
        }

        public static class LocalCityNCOVDataListDTO {
            private boolean isSpecialCity;
            private String province;
            private String mtime;
            private String local_wzz_add;
            private int mediumRiskAreaNum;
            private int local_confirm_add;
            private int highRiskAreaNum;
            private String city;
            private String adcode;
            private String date;
            private boolean isUpdated;

            public boolean isIsSpecialCity() {
                return isSpecialCity;
            }

            public void setIsSpecialCity(boolean isSpecialCity) {
                this.isSpecialCity = isSpecialCity;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getMtime() {
                return mtime;
            }

            public void setMtime(String mtime) {
                this.mtime = mtime;
            }

            public String getLocal_wzz_add() {
                return local_wzz_add;
            }

            public void setLocal_wzz_add(String local_wzz_add) {
                this.local_wzz_add = local_wzz_add;
            }

            public int getMediumRiskAreaNum() {
                return mediumRiskAreaNum;
            }

            public void setMediumRiskAreaNum(int mediumRiskAreaNum) {
                this.mediumRiskAreaNum = mediumRiskAreaNum;
            }

            public int getLocal_confirm_add() {
                return local_confirm_add;
            }

            public void setLocal_confirm_add(int local_confirm_add) {
                this.local_confirm_add = local_confirm_add;
            }

            public int getHighRiskAreaNum() {
                return highRiskAreaNum;
            }

            public void setHighRiskAreaNum(int highRiskAreaNum) {
                this.highRiskAreaNum = highRiskAreaNum;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getAdcode() {
                return adcode;
            }

            public void setAdcode(String adcode) {
                this.adcode = adcode;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public boolean isIsUpdated() {
                return isUpdated;
            }

            public void setIsUpdated(boolean isUpdated) {
                this.isUpdated = isUpdated;
            }
        }
    }
}